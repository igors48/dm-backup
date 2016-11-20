import org.junit.Before;
import org.junit.Test;
import service.error.WrongContentType;
import service.error.WrongResponseCode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static service.Loader.validateLoginResponse;

/**
 * Created by igor on 20.11.2016.
 */
public class LoginResponseValidationTest {

    private static final String WRONG = "WRONG";

    private int statusCode;
    private String contentType;

    @Before
    public void setUp() throws Exception {
        this.statusCode = 302;
        this.contentType = "text/html";
    }

    @Test
    public void happyFlow() throws Exception {

        try {
            validateLoginResponse(this.statusCode, this.contentType);
        } catch (Exception exception) {
            fail();
        }

    }

    @Test
    public void wrongStatusCode() throws Exception {

        try {
            validateLoginResponse(48, this.contentType);

            fail();
        } catch (Exception exception) {
            assertEquals(new WrongResponseCode(302, 48), exception);
        }

    }

    @Test
    public void wrongContentType() throws Exception {

        try {
            validateLoginResponse(this.statusCode, WRONG);

            fail();
        } catch (Exception exception) {
            assertEquals(new WrongContentType(WRONG), exception);
        }

    }

}
