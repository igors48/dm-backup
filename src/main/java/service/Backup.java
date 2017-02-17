package service;

import com.google.appengine.api.datastore.Transaction;
import service.configuration.Recipients;
import service.error.ServiceException;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static util.Assert.guard;
import static util.Parameter.notNull;
import static util.TransactionTools.rollbackIfActive;

/**
 * Created by igor on 14.11.2016.
 */
public class Backup {

    private static final Logger LOGGER = Logger.getLogger(Backup.class.getName());

    private final Loader loader;
    private final Sender sender;
    private final ChangesDetector changesDetector;
    private final Recipients recipients;
    private final ContentStore contentStore;
    private final Transactions transactions;

    public Backup(final Loader loader, final Sender sender, final ChangesDetector changesDetector, final Recipients recipients, final ContentStore contentStore, final Transactions transactions) {
        guard(notNull(this.loader = loader));
        guard(notNull(this.sender = sender));
        guard(notNull(this.changesDetector = changesDetector));
        guard(notNull(this.recipients = recipients));
        guard(notNull(this.contentStore = contentStore));
        guard(notNull(this.transactions = transactions));
    }

    public void execute() {

        try {
            LOGGER.info("Backup started");

            final Content content = this.loader.load();

            final Action action = this.changesDetector.getActionForContent(content.file);
            LOGGER.info(String.format("Action [ %s ]", action));

            switch (action) {
                case NO_ACTION:
                    break;
                case SAVE:
                    storeContent(content);
                    break;
                case UPDATE_LAST:
                    updateLast(content);
                    break;
                case SEND:
                    sendContent(content);
                    break;
            }

            LOGGER.info("Backup finished");
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, "Backup failed", exception);

            sendError(exception);
        }
    }

    private void updateLast(final Content content) {
        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            this.contentStore.updateLast(content);

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    private void storeContent(final Content content) {
        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            this.contentStore.store(content);

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    private void sendContent(final Content content) {

        for (final String recipient : this.recipients.contentRecipients) {
            sendContent(recipient, content);
        }
    }

    private void sendContent(final String recipient, final Content content) {

        try {
            this.sender.sendContent(recipients.adminRecipient, recipient, content);
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Sending content to [ %s ] failed", recipient), exception);

            this.sendError(exception);
        }
    }

    private void sendError(final ServiceException exception) {
        final String errorRecipient = recipients.adminRecipient;

        try {
            this.sender.sendException(errorRecipient, exception);
        } catch (ServiceException anotherException) {
            LOGGER.log(Level.SEVERE, format("Sending error report to [ %s ] failed", errorRecipient), anotherException);
        }
    }

}
