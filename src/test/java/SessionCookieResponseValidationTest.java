import org.junit.Before;
import org.junit.Test;
import service.error.InvalidSessionId;
import service.error.WrongContentType;
import service.error.WrongResponseCode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static service.Loader.validateSessionCookieResponse;

/**
 * Created by igor on 20.11.2016.
 */
public class SessionCookieResponseValidationTest {

    private static final String WRONG = "WRONG";

    private int statusCode;
    private String contentType;
    private String cookie;

    @Before
    public void setUp() throws Exception {
        this.statusCode = 200;
        this.contentType = "text/html";
        this.cookie = "PHPSESSID-1";
    }

    @Test
    public void happyFlow() throws Exception {

        try {
            validateSessionCookieResponse(this.statusCode, this.contentType, this.cookie);
        } catch (Exception exception) {
            fail();
        }
    }

    @Test
    public void wrongStatusCode() throws Exception {

        try {
            validateSessionCookieResponse(48, this.contentType, this.cookie);

            fail();
        } catch (Exception exception) {
            assertEquals(new WrongResponseCode(200, 48), exception);
        }
    }

    @Test
    public void wrongContentType() throws Exception {

        try {
            validateSessionCookieResponse(this.statusCode, WRONG, this.cookie);

            fail();
        } catch (Exception exception) {
            assertEquals(new WrongContentType(WRONG), exception);
        }
    }

    @Test
    public void wrongSessionId() throws Exception {

        try {
            validateSessionCookieResponse(this.statusCode, this.contentType, WRONG);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidSessionId(WRONG), exception);
        }
    }

}
