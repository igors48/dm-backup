package service;

import service.configuration.Recipients;
import service.error.ServiceException;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 14.11.2016.
 */
public class Backup {

    private static final Logger LOGGER = Logger.getLogger(Backup.class.getName());

    private final Loader loader;
    private final Sender sender;
    private final ChangesDetector changesDetector;
    private final Recipients recipients;

    public Backup(final Loader loader, final Sender sender, final ChangesDetector changesDetector, final Recipients recipients) {
        guard(notNull(this.loader = loader));
        guard(notNull(this.sender = sender));
        guard(notNull(this.changesDetector = changesDetector));
        guard(notNull(this.recipients = recipients));
    }

    public void execute() {

        try {
            LOGGER.info("Backup started");

            final String content = this.loader.load();

            boolean contentMustBeSent = this.changesDetector.contentMustBeSent(content);

            if (contentMustBeSent) {
                storeContent(content);
                sendContent(content);
            }

            LOGGER.info("Backup finished");
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, "Backup failed", exception);

            sendError(exception);
        }
    }

    private void storeContent(final String content) {

    }

    private void sendContent(final String content) {

        for (final String recipient : this.recipients.contentRecipients) {
            sendContent(recipient, content);
        }
    }

    private void sendContent(final String recipient, final String content) {

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
