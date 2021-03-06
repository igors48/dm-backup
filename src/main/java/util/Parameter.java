package util;

import org.apache.commons.validator.routines.EmailValidator;
import util.apache.DomainValidator;
import util.apache.UrlValidator;

import java.util.regex.Pattern;

import static java.util.UUID.fromString;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.04.13
 */
public final class Parameter {

    private static final String[] SCHEMES = {"http", "https"};
    private static final UrlValidator URL_VALIDATOR = new UrlValidator(SCHEMES);
    private static final DomainValidator DOMAIN_VALIDATOR = DomainValidator.getInstance();
    private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();

    private static final Pattern FILE_NAME_CHARS = Pattern.compile("[_a-zA-Z0-9\\-\\.]+");
    private static final Pattern FORM_DATA = Pattern.compile("(([^=]+)=([^&]+))*");

    public static boolean isContainOnlyFileNameChars(final String value) {
        return FILE_NAME_CHARS.matcher(value).matches();
    }

    public static boolean isValidUuid(final String value) {

        try {
            fromString(value);

            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean isValidString(final String value) {
        return value != null && !value.isEmpty();
    }

    public static boolean isPositive(final long value) {
        return value >= 0;
    }

    public static boolean notNull(final Object value) {
        return value != null;
    }

    public static boolean isValidUrl(final String value) {
        return notNull(value) && URL_VALIDATOR.isValid(value.toLowerCase());
    }

    public static boolean isValidDomain(final String value) {
        return notNull(value) && DOMAIN_VALIDATOR.isValid(value.toLowerCase());
    }

    public static boolean isValidEmail(final String value) {
        return notNull(value) && EMAIL_VALIDATOR.isValid(value.toLowerCase());
    }

    public static boolean isValidFormData(final String value) {
        return notNull(value) && FORM_DATA.matcher(value).matches();
    }

    private Parameter() {
        // empty
    }

}
