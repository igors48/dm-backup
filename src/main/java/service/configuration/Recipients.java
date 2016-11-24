package service.configuration;

import java.util.List;

import static util.Assert.guard;
import static util.Parameter.isValidEmail;

/**
 * Created by igor on 14.11.2016.
 */
public class Recipients {

    public final String adminRecipient;
    public final List<String> contentRecipients;

    public Recipients(final String adminRecipient, final List<String> contentRecipients) {
        guard(isValidRecipient(this.adminRecipient = adminRecipient));
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

        if (!adminRecipient.equals(that.adminRecipient)) return false;

        return contentRecipients.equals(that.contentRecipients);
    }

    @Override
    public int hashCode() {
        int result = adminRecipient.hashCode();
        result = 31 * result + contentRecipients.hashCode();

        return result;
    }

}
