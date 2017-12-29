package service.configuration;

import service.cron.CronJobConfiguration;
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

    public static final String ORIGIN = "origin";
    public static final String HOST = "host";

    public static final String LOGIN_URL = "login.url";
    public static final String LOGIN_REFERER = "login.referer";
    public static final String LOGIN_DATA = "login.data";

    public static final String DOWNLOAD_URL = "download.url";
    public static final String DOWNLOAD_REFERER = "download.referer";
    public static final String DOWNLOAD_DATA = "download.data";

    public static final String ACCOUNTS_URL = "accounts.url";
    public static final String ACCOUNTS_REFERER = "accounts.referer";

    public static final String ADMIN = "admin";
    public static final String RECIPIENTS = "recipients";
    public static final String RECIPIENT = "recipient";

    public static final String WAIT_TIME_MILLIS = "wait.time.millis";

    public static final String SNAPSHOTS_STORE_CAPACITY = "snapshots.store.capacity";

    public static final String CRON = "cron";
    public static final String RUSH_HOUR = "rush.hour";
    public static final String MAX_CONSECUTIVE_ERRORS_COUNT = "max.consecutive.errors.count";

    public static final String APP_VERSION = "app.version";

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

    private final int snapshotsStoreCapacity;

    private final int rushHour;
    private final int maxConsecutiveErrorsCount;

    private final String appVersion;

    public Configuration(final String origin, final String host, final String loginUrl, final String loginReferer, final String loginData, final String downloadUrl, final String downloadReferer, final String downloadData, final String accountsUrl, final String accountsReferer, final String admin, final List<String> recipients, final String waitTimeMillisAsString, final String snapshotsStoreCapacityAsString, final String rushHourAsString, final String maxConsecutiveErrorsCountAsString, final String appVersion) throws ServiceException {
        guard(isValidUrl(this.origin = origin), new InvalidConfigurationParameter(ORIGIN, origin));
        guard(isValidDomain(this.host = host), new InvalidConfigurationParameter(HOST, host));

        guard(isValidUrl(this.loginUrl = loginUrl), new InvalidConfigurationParameter(LOGIN_URL, loginUrl));
        guard(isValidUrl(this.loginReferer = loginReferer), new InvalidConfigurationParameter(LOGIN_REFERER, loginReferer));
        guard(isValidFormData(this.loginData = loginData), new InvalidConfigurationParameter(LOGIN_DATA, loginData));

        guard(isValidUrl(this.downloadUrl = downloadUrl), new InvalidConfigurationParameter(DOWNLOAD_URL, downloadUrl));
        guard(isValidUrl(this.downloadReferer = downloadReferer), new InvalidConfigurationParameter(DOWNLOAD_REFERER, downloadReferer));
        guard(isValidFormData(this.downloadData = downloadData), new InvalidConfigurationParameter(DOWNLOAD_DATA, downloadData));

        guard(isValidUrl(this.accountsUrl = accountsUrl), new InvalidConfigurationParameter(ACCOUNTS_URL, accountsUrl));
        guard(isValidUrl(this.accountsReferer = accountsReferer), new InvalidConfigurationParameter(ACCOUNTS_REFERER, accountsReferer));

        guard(isValidEmail(this.admin = admin), new InvalidConfigurationParameter(ADMIN, admin));

        final int rushHour = convertAsInt(rushHourAsString, new InvalidConfigurationParameter(RUSH_HOUR, rushHourAsString));
        guard(isPositive(this.rushHour = rushHour), new InvalidConfigurationParameter(RUSH_HOUR, rushHourAsString));

        final int maxConsecutiveErrorsCount = convertAsInt(maxConsecutiveErrorsCountAsString, new InvalidConfigurationParameter(MAX_CONSECUTIVE_ERRORS_COUNT, maxConsecutiveErrorsCountAsString));
        guard(isPositive(this.maxConsecutiveErrorsCount = maxConsecutiveErrorsCount), new InvalidConfigurationParameter(MAX_CONSECUTIVE_ERRORS_COUNT, maxConsecutiveErrorsCountAsString));

        guard(notNull(recipients), new InvalidConfigurationParameter(RECIPIENTS, "null"));
        this.recipients = new ArrayList<>();

        for (final String recipient : recipients) {
            guard(isValidEmail(recipient), new InvalidConfigurationParameter(RECIPIENT, recipient));
            this.recipients.add(recipient);
        }

        final long waitTimeMillis = convertAsLong(waitTimeMillisAsString, new InvalidConfigurationParameter(WAIT_TIME_MILLIS, waitTimeMillisAsString));
        guard(isPositive(this.waitTimeMillis = waitTimeMillis), new InvalidConfigurationParameter(WAIT_TIME_MILLIS, String.valueOf(waitTimeMillis)));

        final int snapshotsStoreCapacity = convertAsInt(snapshotsStoreCapacityAsString, new InvalidConfigurationParameter(SNAPSHOTS_STORE_CAPACITY, snapshotsStoreCapacityAsString));
        guard(isPositive(this.snapshotsStoreCapacity = snapshotsStoreCapacity), new InvalidConfigurationParameter(SNAPSHOTS_STORE_CAPACITY, String.valueOf(snapshotsStoreCapacity)));

        guard(isValidString(this.appVersion = appVersion), new InvalidConfigurationParameter(APP_VERSION, appVersion));
    }

    public CronJobConfiguration getCronJobConfiguration() {
        return new CronJobConfiguration(this.maxConsecutiveErrorsCount, this.rushHour, this.getRecipients());
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

    public int getSnapshotsStoreCapacity() {
        return this.snapshotsStoreCapacity;
    }

    private long convertAsLong(final String value, final ServiceException exception) throws ServiceException {
        try {
            return Long.valueOf(value);
        } catch (Exception e) {
            throw exception;
        }
    }

    private int convertAsInt(final String value, final ServiceException exception) throws ServiceException {
        try {
            return Integer.valueOf(value);
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
        final String snapshotsStoreCapacity = System.getProperty(SNAPSHOTS_STORE_CAPACITY);
        final String rushHour = System.getProperty(RUSH_HOUR);
        final String maxConsecutiveErrorsCount = System.getProperty(MAX_CONSECUTIVE_ERRORS_COUNT);

        final String appVersionAsString = System.getProperty(APP_VERSION);

        return new Configuration(origin, host, loginUrl, loginReferer, loginData, downloadUrl, downloadReferer, downloadData, accountsUrl, accountsReferer, admin, recipients, waitTimeMillis, snapshotsStoreCapacity, rushHour, maxConsecutiveErrorsCount, appVersionAsString);
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "origin='" + origin + '\'' +
                ", host='" + host + '\'' +
                ", loginUrl='" + loginUrl + '\'' +
                ", loginReferer='" + loginReferer + '\'' +
                ", loginData='" + loginData + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", downloadReferer='" + downloadReferer + '\'' +
                ", downloadData='" + downloadData + '\'' +
                ", accountsUrl='" + accountsUrl + '\'' +
                ", accountsReferer='" + accountsReferer + '\'' +
                ", admin='" + admin + '\'' +
                ", recipients=" + recipients +
                ", waitTimeMillis=" + waitTimeMillis +
                ", snapshotsStoreCapacity=" + snapshotsStoreCapacity +
                ", rushHour=" + rushHour +
                ", maxConsecutiveErrorsCount=" + maxConsecutiveErrorsCount +
                ", appVersion='" + appVersion + '\'' +
                '}';
    }

}
