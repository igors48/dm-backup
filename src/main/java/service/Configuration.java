package service;

import service.error.InvalidConfigurationParameter;

import static util.Assert.guard;
import static util.Parameter.*;

/**
 * Created by igor on 19.11.2016.
 */
public class Configuration {

    private static final String ORIGIN = "origin";
    private static final String HOST = "host";

    private static final String LOGIN_URL = "login.url";
    private static final String LOGIN_REFERER = "login.referer";
    private static final String LOGIN_DATA = "login.data";

    private static final String DOWNLOAD_URL = "download.url";
    private static final String DOWNLOAD_REFERER = "download.referer";
    private static final String DOWNLOAD_DATA = "download.data";

    private static final String ADMIN = "admin";
    private static final String RECIPIENTS = "recipients";

    private final String origin;
    private final String host;

    private final String loginUrl;
    private final String loginReferer;
    private final String loginData;

    private final String downloadUrl;
    private final String downloadReferer;
    private final String downloadData;

    private final String admin;
    private final String recipients;

    public Configuration(final String origin, final String host, final String loginUrl, final String loginReferer, final String loginData, final String downloadUrl, final String downloadReferer, final String downloadData, final String admin, final String recipients) throws ServiceException {
        guard(isValidDomain(this.origin = origin), new InvalidConfigurationParameter("origin", origin));
        guard(isValidUrl(this.host = host), new InvalidConfigurationParameter("host", host));

        guard(isValidUrl(this.loginUrl = loginUrl), new InvalidConfigurationParameter("loginUrl", loginUrl));
        guard(isValidUrl(this.loginReferer = loginReferer), new InvalidConfigurationParameter("loginReferer", loginReferer));
        this.loginData = loginData;

        guard(isValidUrl(this.downloadUrl = downloadUrl), new InvalidConfigurationParameter("downloadUrl", downloadUrl));
        guard(isValidUrl(this.downloadReferer = downloadReferer), new InvalidConfigurationParameter("downloadReferer", downloadReferer));
        this.downloadData = downloadData;

        guard(isValidEmail(this.admin = admin), new InvalidConfigurationParameter("admin", admin));
        this.recipients = recipients;
    }

    public static Configuration fromSystemProperties() throws ServiceException {
        final String origin = System.getProperty(ORIGIN);
        final String host = System.getProperty(HOST);
        final String loginUrl = System.getProperty(LOGIN_URL);
        final String loginReferer = System.getProperty(LOGIN_REFERER);
        final String loginData = System.getProperty(LOGIN_DATA);
        final String downloadUrl = System.getProperty(DOWNLOAD_URL);
        final String downloadReferer = System.getProperty(DOWNLOAD_REFERER);
        final String downloadData = System.getProperty(DOWNLOAD_DATA);
        final String admin = System.getProperty(ADMIN);
        final String recipients = System.getProperty(RECIPIENTS);

        return new Configuration(origin, host, loginUrl, loginReferer, loginData, downloadUrl, downloadReferer, downloadData, admin, recipients);
    }

}
