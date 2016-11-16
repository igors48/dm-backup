package service;

import service.error.*;
import util.ConnectionTools;

import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;

import static util.Assert.guard;
import static util.ConnectionTools.UTF_8;
import static util.ConnectionTools.disconnect;
import static util.Parameter.notNull;

/**
 * Created by igor on 14.11.2016.
 */
public class Loader {

    private final AccessParameters accessParameters;

    public Loader(final AccessParameters accessParameters) {
        guard(notNull(this.accessParameters = accessParameters));
    }

    public String load() throws ServiceException {

        try {
            return downloadFile(login(getSession()));
        } catch (IOException exception) {
            throw new ServiceException(new IoError(exception.getMessage()));
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
            final CookieManager cookieManager = new CookieManager();
            cookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));

            final String sessionCookie = cookieManager.getCookieStore().getCookies().get(0) + ";";

            validateSessionCookieResponse(responseCode, contentType, sessionCookie);

            return sessionCookie;
        } finally {
            disconnect(connection);
        }
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

    private HttpURLConnection postForm(final FormParameters formParameters, final String sessionCookie) throws IOException {
        HttpURLConnection connection = ConnectionTools.setupConnection(formParameters.url, ConnectionTools.Method.POST);

        connection.setRequestProperty("Host", this.accessParameters.general.host);
        connection.setRequestProperty("Origin", this.accessParameters.general.origin);
        connection.setRequestProperty("Referer", formParameters.referer);
        connection.setRequestProperty("Cookie", sessionCookie);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Upgrade-Insecure-Requests", "1");

        connection.setInstanceFollowRedirects(false);

        connection.connect();

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(formParameters.data.getBytes(UTF_8));
        outputStream.close();

        return connection;
    }

    public static void validateSessionCookieResponse(final int statusCode, final String contentType, final String cookie) throws ServiceException {
        guard(statusCode == 200, new WrongResponseCode(200, statusCode));
        guard(contentType.contains("text/html"), new WrongContentType(contentType));
        guard(cookie.startsWith("PHPSESSID"), new InvalidSessionId(cookie));
    }

    public static void validateContentResponse(final int statusCode, final String contentType, final String content) throws ServiceException {
        guard(statusCode == 200, new WrongResponseCode(200, statusCode));
        guard(contentType.contains("text/html"), new WrongContentType(contentType));
        guard(!(content == null || content.isEmpty()), new InvalidContent(content));
    }

    public static void validateLoginResponse(final int statusCode, final String contentType) throws ServiceException {
        guard(statusCode == 302, new WrongResponseCode(302, statusCode));
        guard(contentType.contains("text/html"), new WrongContentType(contentType));
    }

}
