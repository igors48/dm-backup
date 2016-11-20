package service;

import java.util.List;

import static util.Assert.guard;
import static util.Parameter.isValidEmail;

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
        return isValidEmail(recipient);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipients that = (Recipients) o;

        if (!errorRecipient.equals(that.errorRecipient)) return false;

        return contentRecipients.equals(that.contentRecipients);
    }

    @Override
    public int hashCode() {
        int result = errorRecipient.hashCode();
        result = 31 * result + contentRecipients.hashCode();

        return result;
    }

}
