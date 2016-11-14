package service;

import java.util.List;

import static util.Assert.guard;
import static util.Parameter.isValidString;

/**
 * Created by igor on 14.11.2016.
 */
public class Recipients {

    public final String errorRecipient;
    public final List<String> contentRecipients;

    public Recipients(final String errorRecipient, final List<String> contentRecipients) {
        guard(isValidRecipient(this.errorRecipient = errorRecipient));
        guard(isValidRecipients(this.contentRecipients = contentRecipients));
    }

    private static boolean isValidRecipient(final String recipient) {
        return isValidString(recipient);
    }

    private static boolean isValidRecipients(final List<String> recipients) {

        if (recipients == null) {
            return false;
        }

        if (recipients.isEmpty()) {
            return false;
        }

        for (final String recipient : recipients) {

            if (!isValidRecipient(recipient)) {
                return false;
            }
        }

        return true;
    }

}
