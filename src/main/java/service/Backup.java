package service;

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
    private final Recipients recipients;

    public Backup(final Loader loader, final Sender sender, final Recipients recipients) {
        guard(notNull(this.loader = loader));
        guard(notNull(this.sender = sender));
        guard(notNull(this.recipients = recipients));
    }

    public void execute() {

        try {
            final String content = this.loader.load();
            sendContent(content);

            LOGGER.info("Backup ok");
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, "Backup failed", exception);

            sendError(exception.error);
        }
    }

    private void sendContent(final String content) {

        for (final String recipient : this.recipients.contentRecipients) {
            sendContent(recipient, content);
        }
    }

    private void sendContent(final String recipient, final String content) {

        try {
            this.sender.sendContent(recipient, content);
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Sending content to [ %s ] failed", recipient), exception);

            this.sendError(exception.error);
        }
    }

    private void sendError(final ServiceError error) {
        final String errorRecipient = recipients.errorRecipient;

        try {
            this.sender.sendError(errorRecipient, error);
        } catch (ServiceException anotherException) {
            LOGGER.log(Level.SEVERE, format("Sending error report to [ %s ] failed", errorRecipient), anotherException);
        }
    }

}
