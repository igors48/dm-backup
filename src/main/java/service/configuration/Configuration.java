package service.configuration;

import service.error.InvalidConfigurationParameter;
import service.error.ServiceException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private static final String ACCOUNTS_URL = "accounts.url";
    private static final String ACCOUNTS_REFERER = "accounts.referer";

    private static final String ADMIN = "admin";
    private static final String RECIPIENTS = "recipients";

    private static final String WAIT_TIME_MILLIS = "wait.time.millis";
    private static final String APP_VERSION = "app.version";

    private final String origin;
    private final String host;

    private final String loginUrl;
    private final String loginReferer;
    private final String loginData;

    private final String downloadUrl;
    private final String downloadReferer;
    private final String downloadData;

    private final String accountsUrl;
    private final String accountsReferer;

    private final String admin;
    private final List<String> recipients;

    private final long waitTimeMillis;

    private final String appVersion;

    public Configuration(final String origin, final String host, final String loginUrl, final String loginReferer, final String loginData, final String downloadUrl, final String downloadReferer, final String downloadData, final String accountsUrl, final String accountsReferer, final String admin, final List<String> recipients, final String waitTimeMillisAsString, final String appVersion) throws ServiceException {
        guard(isValidUrl(this.origin = origin), new InvalidConfigurationParameter("origin", origin));
        guard(isValidDomain(this.host = host), new InvalidConfigurationParameter("host", host));

        guard(isValidUrl(this.loginUrl = loginUrl), new InvalidConfigurationParameter("loginUrl", loginUrl));
        guard(isValidUrl(this.loginReferer = loginReferer), new InvalidConfigurationParameter("loginReferer", loginReferer));
        guard(isValidFormData(this.loginData = loginData), new InvalidConfigurationParameter("loginData", loginData));

        guard(isValidUrl(this.downloadUrl = downloadUrl), new InvalidConfigurationParameter("downloadUrl", downloadUrl));
        guard(isValidUrl(this.downloadReferer = downloadReferer), new InvalidConfigurationParameter("downloadReferer", downloadReferer));
        guard(isValidFormData(this.downloadData = downloadData), new InvalidConfigurationParameter("downloadData", downloadData));

        guard(isValidUrl(this.accountsUrl = accountsUrl), new InvalidConfigurationParameter("accountsUrl", accountsUrl));
        guard(isValidUrl(this.accountsReferer = accountsReferer), new InvalidConfigurationParameter("accountsReferer", accountsReferer));

        guard(isValidEmail(this.admin = admin), new InvalidConfigurationParameter("admin", admin));

        guard(notNull(recipients), new InvalidConfigurationParameter("recipients", "null"));
        this.recipients = new ArrayList<>();

        for (final String recipient : recipients) {
            guard(isValidEmail(recipient), new InvalidConfigurationParameter("recipient", recipient));
            this.recipients.add(recipient);
        }

        final long waitTimeMillis = convert(waitTimeMillisAsString, new InvalidConfigurationParameter("waitTimeMillis", waitTimeMillisAsString));
        guard(isPositive(this.waitTimeMillis = waitTimeMillis), new InvalidConfigurationParameter("waitTimeMillis", String.valueOf(waitTimeMillis)));

        guard(isValidString(this.appVersion = appVersion), new InvalidConfigurationParameter("app.version", appVersion));
    }

    public AccessParameters getAccessParameters() {
        final GeneralParameters general = new GeneralParameters(this.origin, this.host);
        final FormParameters login = new FormParameters(this.loginUrl, this.loginReferer, this.loginData);
        final FormParameters download = new FormParameters(this.downloadUrl, this.downloadReferer, this.downloadData);
        final FormParameters accounts = new FormParameters(this.accountsUrl, this.accountsReferer, "");

        return new AccessParameters(general, login, download, accounts);
    }

    public Recipients getRecipients() {
        return new Recipients(this.admin, this.recipients);
    }

    public long getWaitTimeMillis() {
        return this.waitTimeMillis;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    private long convert(final String value, final ServiceException exception) throws ServiceException {
        try {
            return Long.valueOf(value);
        } catch (Exception e) {
            throw exception;
        }
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
        final String accountsUrl = System.getProperty(ACCOUNTS_URL);
        final String accountsReferer = System.getProperty(ACCOUNTS_REFERER);
        final String admin = System.getProperty(ADMIN);
        final String recipientsAsString = System.getProperty(RECIPIENTS);
        final List<String> recipients = Arrays.asList(recipientsAsString.split(";"));

        final String waitTimeMillis = System.getProperty(WAIT_TIME_MILLIS);
        final String appVersionAsString = System.getProperty(APP_VERSION);

        return new Configuration(origin, host, loginUrl, loginReferer, loginData, downloadUrl, downloadReferer, downloadData, accountsUrl, accountsReferer, admin, recipients, waitTimeMillis, appVersionAsString);
    }

}
