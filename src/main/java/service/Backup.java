package service;

import com.google.appengine.api.datastore.Transaction;
import service.configuration.Recipients;
import service.error.ServiceException;
import util.account.Account;

import java.util.List;
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
    private final SnapshotStore changesSnapshotStore;
    private final SnapshotStore dailySnapshotStore;
    private final Transactions transactions;

    public Backup(final Loader loader, final Sender sender, final ChangesDetector changesDetector, final Recipients recipients, final SnapshotStore changesSnapshotStore, final SnapshotStore dailySnapshotStore, final Transactions transactions) {
        guard(notNull(this.loader = loader));
        guard(notNull(this.sender = sender));
        guard(notNull(this.changesDetector = changesDetector));
        guard(notNull(this.recipients = recipients));
        guard(notNull(this.changesSnapshotStore = changesSnapshotStore));
        guard(notNull(this.dailySnapshotStore = dailySnapshotStore));
        guard(notNull(this.transactions = transactions));
    }

    public void checkChanges() {

        try {
            LOGGER.info("Check changes started");

            final Content content = this.loader.load();

            final Action action = this.changesDetector.getActionForContent(content.file);
            LOGGER.info(String.format("Action [ %s ]", action));

            switch (action.type) {
                case NO_ACTION:
                    break;
                case SAVE:
                    storeContent(content);
                    break;
                case UPDATE_LAST:
                    updateLast(content);
                    break;
                case SEND:
                    sendChangedContent(content, action.accounts);
                    break;
            }

            LOGGER.info("Check changes finished");
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, "Check changes failed", exception);

            sendError(exception);
        }
    }

    public void dailyBackup() {

        try {
            LOGGER.info("Backup started");

            final Content content = this.loader.load();
            this.dailySnapshotStore.store(content);
            sendDailyBackup(content);

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

            this.changesSnapshotStore.updateLast(content);

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    private void storeContent(final Content content) {
        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            this.changesSnapshotStore.store(content);

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    private void sendChangedContent(final Content content, List<Account> previousAccounts) {

        for (final String recipient : this.recipients.contentRecipients) {
            sendChangedContent(recipient, content, previousAccounts);
        }
    }

    private void sendDailyBackup(final Content content) {

        for (final String recipient : this.recipients.contentRecipients) {
            sendDailyBackup(recipient, content);
        }
    }

    private void sendDailyBackup(final String recipient, final Content content) {

        try {
            this.sender.sendDailyBackup(recipients.adminRecipient, recipient, content);
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Sending daily backup content to [ %s ] failed", recipient), exception);

            this.sendError(exception);
        }
    }

    private void sendChangedContent(final String recipient, final Content content, List<Account> previousAccounts) {

        try {
            this.sender.sendChangedContent(recipients.adminRecipient, recipient, content, previousAccounts);
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Sending changed content to [ %s ] failed", recipient), exception);

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
