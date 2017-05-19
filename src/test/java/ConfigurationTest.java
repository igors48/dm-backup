import org.junit.Before;
import org.junit.Test;
import service.configuration.*;
import service.error.InvalidConfigurationParameter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by igor on 20.11.2016.
 */
public class ConfigurationTest {

    private static final String WRONG = "wrong";

    private String origin;
    private String host;
    private String loginUrl;
    private String loginReferer;
    private String loginData;
    private String downloadUrl;
    private String downloadReferer;
    private String downloadData;
    private String accountsUrl;
    private String accountsReferer;
    private String admin;
    private List<String> recipients;
    private String waitTimeMillisAsString;
    private String snapshotsStoreCapacityAsString;
    private String appVersion;

    @Before
    public void setUp() throws Exception {
        this.origin = "https://google.com";
        this.host = "google.com";
        this.loginUrl = "https://google.com/login";
        this.loginReferer = "https://google.com";
        this.loginData = "a=b&c=d";
        this.downloadUrl = "https://google.com/download";
        this.downloadReferer = "https://google.com/login";
        this.downloadData = "e=f&g=h";
        this.accountsUrl = "https://google.com/download";
        this.accountsReferer = "https://google.com/login";
        this.admin = "g@h.com";
        this.recipients = new ArrayList<String>() {{
            add("a@b.com");
            add("d@e.com");
        }};
        this.waitTimeMillisAsString = "3";
        this.snapshotsStoreCapacityAsString = "5";

        this.appVersion = "42.42";
    }

    @Test
    public void whenAllAreOkThenAllAreOk() {

        try {
            final Configuration configuration = new Configuration(this.origin, this.host, this.loginUrl, this.loginReferer, this.loginData, this.downloadUrl, this.downloadReferer, this.downloadData, this.accountsUrl, this.accountsReferer, this.admin, this.recipients, this.waitTimeMillisAsString, this.snapshotsStoreCapacityAsString, this.appVersion);

            final GeneralParameters general = new GeneralParameters(this.origin, this.host);
            final FormParameters login = new FormParameters(this.loginUrl, this.loginReferer, this.loginData);
            final FormParameters download = new FormParameters(this.downloadUrl, this.downloadReferer, this.downloadData);
            final FormParameters accounts = new FormParameters(this.accountsUrl, this.accountsReferer, "");
            final Recipients recipients = new Recipients(this.admin, this.recipients);
            final AccessParameters accessParameters = new AccessParameters(general, login, download, accounts);

            assertEquals(accessParameters, configuration.getAccessParameters());
            assertEquals(recipients, configuration.getRecipients());

            assertEquals(3, configuration.getWaitTimeMillis());
            assertEquals(5, configuration.getSnapshotsStoreCapacity());

            assertEquals(this.appVersion, configuration.getAppVersion());
        } catch (Exception exception) {
            fail();
        }

    }

    @Test
    public void wrongOrigin() {

        try {
            new Configuration(WRONG, this.host, this.loginUrl, this.loginReferer, this.loginData, this.downloadUrl, this.downloadReferer, this.downloadData, this.accountsUrl, this.accountsReferer, this.admin, this.recipients, this.waitTimeMillisAsString, this.snapshotsStoreCapacityAsString, this.appVersion);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter(Configuration.ORIGIN, WRONG), exception);
        }

    }

    @Test
    public void wrongHost() {

        try {
            new Configuration(this.origin, WRONG, this.loginUrl, this.loginReferer, this.loginData, this.downloadUrl, this.downloadReferer, this.downloadData, this.accountsUrl, this.accountsReferer, this.admin, this.recipients, this.waitTimeMillisAsString, this.snapshotsStoreCapacityAsString, this.appVersion);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter(Configuration.HOST, WRONG), exception);
        }

    }

    @Test
    public void wrongLoginUrl() {

        try {
            new Configuration(this.origin, this.host, WRONG, this.loginReferer, this.loginData, this.downloadUrl, this.downloadReferer, this.downloadData, this.accountsUrl, this.accountsReferer, this.admin, this.recipients, this.waitTimeMillisAsString, this.snapshotsStoreCapacityAsString, this.appVersion);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter(Configuration.LOGIN_URL, WRONG), exception);
        }

    }

    @Test
    public void wrongLoginReferer() {

        try {
            new Configuration(this.origin, this.host, this.loginUrl, WRONG, this.loginData, this.downloadUrl, this.downloadReferer, this.downloadData, this.accountsUrl, this.accountsReferer, this.admin, this.recipients, this.waitTimeMillisAsString, this.snapshotsStoreCapacityAsString, this.appVersion);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter(Configuration.LOGIN_REFERER, WRONG), exception);
        }

    }

    @Test
    public void wrongLoginData() {

        try {
            new Configuration(this.origin, this.host, this.loginUrl, this.loginReferer, WRONG, this.downloadUrl, this.downloadReferer, this.downloadData, this.accountsUrl, this.accountsReferer, this.admin, this.recipients, this.waitTimeMillisAsString, this.snapshotsStoreCapacityAsString, this.appVersion);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter(Configuration.LOGIN_DATA, WRONG), exception);
        }

    }

    @Test
    public void wrongDownloadUrl() {

        try {
            new Configuration(this.origin, this.host, this.loginUrl, this.loginReferer, this.loginData, WRONG, this.downloadReferer, this.downloadData, this.accountsUrl, this.accountsReferer, this.admin, this.recipients, this.waitTimeMillisAsString, this.snapshotsStoreCapacityAsString, this.appVersion);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter(Configuration.DOWNLOAD_URL, WRONG), exception);
        }

    }

    @Test
    public void wrongDownloadReferer() {

        try {
            new Configuration(this.origin, this.host, this.loginUrl, this.loginReferer, this.loginData, this.downloadUrl, WRONG, this.downloadData, this.accountsUrl, this.accountsReferer, this.admin, this.recipients, this.waitTimeMillisAsString, this.snapshotsStoreCapacityAsString, this.appVersion);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter(Configuration.DOWNLOAD_REFERER, WRONG), exception);
        }

    }

    @Test
    public void wrongDownloadData() {

        try {
            new Configuration(this.origin, this.host, this.loginUrl, this.loginReferer, this.loginData, this.downloadUrl, this.downloadReferer, WRONG, this.accountsUrl, this.accountsReferer, this.admin, this.recipients, this.waitTimeMillisAsString, this.snapshotsStoreCapacityAsString, this.appVersion);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter(Configuration.DOWNLOAD_DATA, WRONG), exception);
        }

    }

    @Test
    public void wrongAdmin() {

        try {
            new Configuration(this.origin, this.host, this.loginUrl, this.loginReferer, this.loginData, this.downloadUrl, this.downloadReferer, this.downloadData, this.accountsUrl, this.accountsReferer, WRONG, this.recipients, this.waitTimeMillisAsString, this.snapshotsStoreCapacityAsString, this.appVersion);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter(Configuration.ADMIN, WRONG), exception);
        }

    }

    @Test
    public void wrongRecipient() {

        try {
            this.recipients.add(WRONG);

            new Configuration(this.origin, this.host, this.loginUrl, this.loginReferer, this.loginData, this.downloadUrl, this.downloadReferer, this.downloadData, this.accountsUrl, this.accountsReferer, this.admin, this.recipients, this.waitTimeMillisAsString, this.snapshotsStoreCapacityAsString, this.appVersion);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter(Configuration.RECIPIENT, WRONG), exception);
        }

    }

    @Test
    public void wrongWaitTimeMillis() {

        try {
            new Configuration(this.origin, this.host, this.loginUrl, this.loginReferer, this.loginData, this.downloadUrl, this.downloadReferer, this.downloadData, this.accountsUrl, this.accountsReferer, this.admin, this.recipients, WRONG, this.snapshotsStoreCapacityAsString, this.appVersion);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter(Configuration.WAIT_TIME_MILLIS, WRONG), exception);
        }
    }

    @Test
    public void wrongAccountsUrl() {

        try {
            new Configuration(this.origin, this.host, this.loginUrl, this.loginReferer, this.loginData, this.downloadUrl, this.downloadReferer, this.downloadData, WRONG, this.accountsReferer, this.admin, this.recipients, this.waitTimeMillisAsString, this.snapshotsStoreCapacityAsString, this.appVersion);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter(Configuration.ACCOUNTS_URL, WRONG), exception);
        }

    }

    @Test
    public void wrongAccountsReferer() {

        try {
            new Configuration(this.origin, this.host, this.loginUrl, this.loginReferer, this.loginData, this.downloadUrl, this.downloadReferer, this.downloadData, this.accountsUrl, WRONG, this.admin, this.recipients, this.waitTimeMillisAsString, this.snapshotsStoreCapacityAsString, this.appVersion);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter(Configuration.ACCOUNTS_REFERER, WRONG), exception);
        }

    }

    @Test
    public void wrongAppVersion() {

        try {
            new Configuration(this.origin, this.host, this.loginUrl, this.loginReferer, this.loginData, this.downloadUrl, this.downloadReferer, this.downloadData, this.accountsUrl, this.accountsReferer, this.admin, this.recipients, this.waitTimeMillisAsString, this.snapshotsStoreCapacityAsString, "");

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter(Configuration.APP_VERSION, ""), exception);
        }

    }

    @Test
    public void wrongSnapshotsStoreCapacity() {

        try {
            new Configuration(this.origin, this.host, this.loginUrl, this.loginReferer, this.loginData, this.downloadUrl, this.downloadReferer, this.downloadData, this.accountsUrl, this.accountsReferer, this.admin, this.recipients, this.waitTimeMillisAsString, WRONG, this.appVersion);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter(Configuration.SNAPSHOTS_STORE_CAPACITY, WRONG), exception);
        }

    }

}
