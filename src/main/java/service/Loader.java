package service;

import service.configuration.AccessParameters;
import service.configuration.FormParameters;
import service.error.*;
import util.ConnectionTools;
import util.account.Account;
import util.account.AccountsParser;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static util.Assert.guard;
import static util.ConnectionTools.UTF_8;
import static util.ConnectionTools.disconnect;
import static util.Parameter.isValidString;
import static util.Parameter.notNull;

/**
 * Created by igor on 14.11.2016.
 */
public class Loader {

    private static final Logger LOGGER = Logger.getLogger(Loader.class.getName());

    private static final Pattern SESSION_COOKIE_PATTERN = Pattern.compile("(PHPSESSID=.*);.*");

    private final AccessParameters accessParameters;

    public Loader(final AccessParameters accessParameters) {
        guard(notNull(this.accessParameters = accessParameters));
    }

    public Content load() throws ServiceException {

        try {
            final String session = login(getSession());

            final List<Account> accounts = parseAccounts(session);
            final String file = downloadFile(session);

            return new Content(accounts, file);
        } catch (IOException exception) {
            throw new IoError(exception.getMessage());
        }
    }

    private String getSession() throws IOException, ServiceException {
        HttpURLConnection connection = null;

        try {
            connection = ConnectionTools.setupConnection(this.accessParameters.general.origin, ConnectionTools.Method.GET);
            connection.connect();

            final int responseCode = connection.getResponseCode();
            final String contentType = connection.getHeaderField(ConnectionTools.CONTENT_TYPE);
            final String cookie = connection.getHeaderField("Set-Cookie");
            final String sessionCookie = parseSessionCookie(cookie);

            validateSessionCookieResponse(responseCode, contentType, sessionCookie);

            return sessionCookie;
        } finally {
            disconnect(connection);
        }
    }

    private List<Account> parseAccounts(final String session) {
        HttpURLConnection connection = null;

        final List<Account> result = new ArrayList<>();

        try {
            connection = ConnectionTools.setupConnection(this.accessParameters.accounts.url, ConnectionTools.Method.GET);
            configureConnection(connection, this.accessParameters.accounts.referer, session);

            connection.connect();

            final int responseCode = connection.getResponseCode();
            final String contentType = connection.getHeaderField(ConnectionTools.CONTENT_TYPE);
            final String content = ConnectionTools.readStringFromConnection(connection);

            final boolean isResponseValid = validateAccountsResponse(responseCode, contentType);

            if (isResponseValid) {
                final List<Account> accounts = AccountsParser.parse(content);
                result.addAll(accounts);
            }
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Error parsing acounts", exception);
        } finally {
            disconnect(connection);
        }

        return result;
    }

    private String login(final String session) throws IOException, ServiceException {
        HttpURLConnection connection = null;

        try {
            connection = postForm(this.accessParameters.login, session);

            final int responseCode = connection.getResponseCode();
            final String contentType = connection.getHeaderField(ConnectionTools.CONTENT_TYPE);

            validateLoginResponse(responseCode, contentType);

            return session;
        } finally {
            disconnect(connection);
        }
    }

    private String downloadFile(final String session) throws IOException, ServiceException {
        HttpURLConnection connection = null;

        try {
            connection = postForm(this.accessParameters.download, session);

            final int responseCode = connection.getResponseCode();
            final String contentType = connection.getHeaderField(ConnectionTools.CONTENT_TYPE);
            final String content = ConnectionTools.readStringFromConnection(connection);

            validateContentResponse(responseCode, contentType, content);

            return content;
        } finally {
            disconnect(connection);
        }
    }

    private HttpURLConnection postForm(final FormParameters formParameters, final String session) throws IOException {
        HttpURLConnection connection = ConnectionTools.setupConnection(formParameters.url, ConnectionTools.Method.POST);

        configureConnection(connection, formParameters.referer, session);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        connection.connect();

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(formParameters.data.getBytes(UTF_8));
        outputStream.close();

        return connection;
    }

    private void configureConnection(final HttpURLConnection connection, final String referer, final String sessionCookie) {
        connection.setRequestProperty("Host", this.accessParameters.general.host);
        connection.setRequestProperty("Origin", this.accessParameters.general.origin);
        connection.setRequestProperty("Referer", referer);
        connection.setRequestProperty("Cookie", sessionCookie);
        connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        connection.setInstanceFollowRedirects(false);
    }

    public static void validateSessionCookieResponse(final int statusCode, final String contentType, final String cookie) throws ServiceException {
        guard(statusCode == 200, new WrongResponseCode(200, statusCode));
        guard(contentType.contains("text/html"), new WrongContentType(contentType));
        guard(cookie.startsWith("PHPSESSID"), new InvalidSessionId(cookie));
    }

    public static void validateContentResponse(final int statusCode, final String contentType, final String content) throws ServiceException {
        guard(statusCode == 200, new WrongResponseCode(200, statusCode));
        guard(contentType.contains("text/csv"), new WrongContentType(contentType));
        guard(isValidString(content), new InvalidContent(content));
    }

    public static void validateLoginResponse(final int statusCode, final String contentType) throws ServiceException {
        guard(statusCode == 302, new WrongResponseCode(302, statusCode));
        guard(contentType.contains("text/html"), new WrongContentType(contentType));
    }

    public static String parseSessionCookie(final String cookie) {

        if (cookie == null) {
            return "";
        }

        final Matcher matcher = SESSION_COOKIE_PATTERN.matcher(cookie);

        return matcher.matches() ? matcher.group(1) + ";" : "";
    }

    public static boolean validateAccountsResponse(final int responseCode, final String contentType) {
        return responseCode == 200 && "text/json; charset=utf-8".equals(contentType);
    }

}
