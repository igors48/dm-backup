package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/**
 * @author : igu
 */
public class ConnectionTools {

    private static final Logger LOGGER = Logger.getLogger(ConnectionTools.class.getName());

    public static final String CONTENT_ENCODING = "content-encoding";
    public static final String CONTENT_TYPE = "content-type";

    public static final String GZIP = "gzip";

    public static final String UTF_8 = "UTF-8";

    public static enum Method {

        GET("GET"),
        POST("POST");

        public final String method;

        Method(final String method) {
            this.method = method;
        }
    }

    private static final int BUFFER_LENGTH = 16384;

    public static HttpURLConnection setupConnection(final String link, final Method method) throws IOException {
        final URL url = new URL(link);

        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod(method.method);

        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36");
        connection.setRequestProperty("Accept-Encoding", "gzip");
        connection.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4");

        return connection;
    }

    public static String readStringFromConnection(final HttpURLConnection connection) throws IOException {
        InputStream inputStream = null;

        try {
            connection.connect();

            inputStream = connection.getInputStream();

            final Map<String, List<String>> headers = connection.getHeaderFields();
            final String charset = getCharset(headers);

            final boolean gZipped = ifGZipped(headers);
            final byte[] bytes = gZipped ? readFullyAsGZipped(inputStream) : readFullyAsUncompressed(inputStream);

            return new String(bytes, charset);
        } finally {
            CloseableTools.close(inputStream);
        }
    }

    public static byte[] readAllBytesFromConnection(final HttpURLConnection connection) throws IOException {
        InputStream inputStream = null;

        try {
            connection.connect();

            inputStream = connection.getInputStream();

            final Map<String, List<String>> headers = connection.getHeaderFields();
            final boolean gZipped = ifGZipped(headers);

            return gZipped ? readFullyAsGZipped(inputStream) : readFullyAsUncompressed(inputStream);
        } finally {
            CloseableTools.close(inputStream);
        }
    }

    public static String getCharset(final Map<String, List<String>> headers) {

        for (final String header : headers.keySet()) {

            if (header == null) {
                continue;
            }

            if (header.equalsIgnoreCase(CONTENT_TYPE)) {
                final List<String> values = headers.get(header);

                for (final String value : values) {
                    final String charset = CharsetTools.findCharSet(value);

                    if (charset != null) {
                        return charset.endsWith(";") ? charset.substring(0, charset.length() - 1) : charset;
                    }
                }
            }
        }

        return UTF_8;
    }

    public static boolean ifGZipped(final Map<String, List<String>> headers) {

        for (final String header : headers.keySet()) {

            if (header == null) {
                continue;
            }

            if (header.equalsIgnoreCase(CONTENT_ENCODING)) {
                final List<String> values = headers.get(header);

                for (final String value : values) {

                    if (value.equalsIgnoreCase(GZIP)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static byte[] readFullyAsUncompressed(final InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        final byte[] data = new byte[BUFFER_LENGTH];

        int read;

        while ((read = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, read);
        }

        buffer.flush();

        return buffer.toByteArray();
    }

    public static byte[] readFullyAsGZipped(final InputStream inputStream) throws IOException {
        final byte[] compressed = readFullyAsUncompressed(inputStream);
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressed);

        GZIPInputStream gZipInputStream = null;

        try {
            gZipInputStream = new GZIPInputStream(byteArrayInputStream);

            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            final byte data[] = new byte[BUFFER_LENGTH];

            int result = 0;

            while (result >= 0) {
                result = gZipInputStream.read(data, 0, data.length);

                if (result > 0) {
                    buffer.write(data, 0, result);
                }
            }

            buffer.flush();

            return buffer.toByteArray();
        } finally {
            CloseableTools.close(gZipInputStream);
        }
    }

    public static void disconnect(final HttpURLConnection connection) {

        try {
            connection.disconnect();
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Error while disconnect", exception);
        }
    }

    private ConnectionTools() {
        // empty
    }

}
