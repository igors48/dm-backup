import org.junit.Before;
import org.junit.Test;
import service.*;
import service.error.InvalidConfigurationParameter;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by igor on 20.11.2016.
 */
public class ConfigurationTest {

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
        this.recipients = Arrays.asList("a@b.com", "d@e.com");
    }

    @Test
    public void whenAllAreOkThenAllAreOk() {
        try {
            final Configuration configuration = new Configuration(this.origin, this.host, this.loginUrl, this.loginReferer, this.loginData, this.downloadUrl, this.downloadReferer, this.downloadData, this.admin, this.recipients);

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
            new Configuration("wrong", this.host, this.loginUrl, this.loginReferer, this.loginData, this.downloadUrl, this.downloadReferer, this.downloadData, this.admin, this.recipients);
        } catch (Exception exception) {
            assertEquals(new InvalidConfigurationParameter("origin", "wrong"), exception);
        }

    }
}
