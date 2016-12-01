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
    private String admin;
    private List<String> recipients;
    private String waitTimeMillisAsString;

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
        this.admin = "g@h.com";
        this.recipients = new ArrayList<String>() {{
            add("a@b.com");
            add("d@e.com");
        }};
        this.waitTimeMillisAsString = "3";
    }

    @Test
    public void whenAllAreOkThenAllAreOk() {

        try {
            final Configuration configuration = new Configuration(this.origin, this.host, this.loginUrl, this.loginReferer, this.loginData, this.downloadUrl, this.downloadReferer, this.downloadData, this.admin, this.recipients, this.waitTimeMillisAsString);

            final GeneralParameters general = new GeneralParameters(this.origin, this.host);
            final FormParameters login = new FormParameters(this.loginUrl, this.loginReferer, this.loginData);
            final FormParameters download = new FormParameters(this.downloadUrl, this.downloadReferer, this.downloadData);
            final Recipients recipients = new Recipients(this.admin, this.recipients);
            final AccessParameters accessParameters = new AccessParameters(general, login, download);

            assertEquals(accessParameters, configuration.getAccessParameters());
            assertEquals(recipients, configuration.getRecipients());

        } catch (Exception exception) {
            fail();
        }

    }

    @Test
    public void wrongOrigin() {

        try {
            new Configuration(WRONG, this.host, this.loginUrl, this.loginReferer, this.loginData, this.downloadUrl, this.downloadReferer, this.downloadData, this.admin, this.recipients, this.waitTimeMillisAsString);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter("origin", WRONG), exception);
        }

    }

    @Test
    public void wrongHost() {

        try {
            new Configuration(this.origin, WRONG, this.loginUrl, this.loginReferer, this.loginData, this.downloadUrl, this.downloadReferer, this.downloadData, this.admin, this.recipients, this.waitTimeMillisAsString);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter("host", WRONG), exception);
        }

    }

    @Test
    public void wrongLoginUrl() {

        try {
            new Configuration(this.origin, this.host, WRONG, this.loginReferer, this.loginData, this.downloadUrl, this.downloadReferer, this.downloadData, this.admin, this.recipients, this.waitTimeMillisAsString);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter("loginUrl", WRONG), exception);
        }

    }

    @Test
    public void wrongLoginReferer() {

        try {
            new Configuration(this.origin, this.host, this.loginUrl, WRONG, this.loginData, this.downloadUrl, this.downloadReferer, this.downloadData, this.admin, this.recipients, this.waitTimeMillisAsString);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter("loginReferer", WRONG), exception);
        }

    }

    @Test
    public void wrongLoginData() {

        try {
            new Configuration(this.origin, this.host, this.loginUrl, this.loginReferer, WRONG, this.downloadUrl, this.downloadReferer, this.downloadData, this.admin, this.recipients, this.waitTimeMillisAsString);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter("loginData", WRONG), exception);
        }

    }

    @Test
    public void wrongDownloadUrl() {

        try {
            new Configuration(this.origin, this.host, this.loginUrl, this.loginReferer, this.loginData, WRONG, this.downloadReferer, this.downloadData, this.admin, this.recipients, this.waitTimeMillisAsString);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter("downloadUrl", WRONG), exception);
        }

    }

    @Test
    public void wrongDownloadReferer() {

        try {
            new Configuration(this.origin, this.host, this.loginUrl, this.loginReferer, this.loginData, this.downloadUrl, WRONG, this.downloadData, this.admin, this.recipients, this.waitTimeMillisAsString);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter("downloadReferer", WRONG), exception);
        }

    }

    @Test
    public void wrongDownloadData() {

        try {
            new Configuration(this.origin, this.host, this.loginUrl, this.loginReferer, this.loginData, this.downloadUrl, this.downloadReferer, WRONG, this.admin, this.recipients, this.waitTimeMillisAsString);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter("downloadData", WRONG), exception);
        }

    }

    @Test
    public void wrongAdmin() {

        try {
            new Configuration(this.origin, this.host, this.loginUrl, this.loginReferer, this.loginData, this.downloadUrl, this.downloadReferer, this.downloadData, WRONG, this.recipients, this.waitTimeMillisAsString);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter("admin", WRONG), exception);
        }

    }

    @Test
    public void wrongRecipient() {

        try {
            this.recipients.add(WRONG);

            new Configuration(this.origin, this.host, this.loginUrl, this.loginReferer, this.loginData, this.downloadUrl, this.downloadReferer, this.downloadData, this.admin, this.recipients, this.waitTimeMillisAsString);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter("recipient", WRONG), exception);
        }

    }

    @Test
    public void wrongWaitTimeMillis() {

        try {
            new Configuration(this.origin, this.host, this.loginUrl, this.loginReferer, this.loginData, this.downloadUrl, this.downloadReferer, this.downloadData, this.admin, this.recipients, WRONG);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter("waitTimeMillis", WRONG), exception);
        }

    }

}
