package util;

import java.io.Closeable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.05.13
 */
public final class CloseableTools {

    private static final Logger LOGGER = Logger.getLogger(CloseableTools.class.getName());

    public static void close(final Closeable closeable) {

        try {

            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Error while closing", exception);
        }
    }

    private CloseableTools() {
        // empty
    }

}
