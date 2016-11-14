package util;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.04.13
 */
public final class Assert {

    public static void guard(final boolean value) {

        if (!value) {
            throw new IllegalArgumentException();
        }
    }

    private Assert() {
        // empty
    }
}
