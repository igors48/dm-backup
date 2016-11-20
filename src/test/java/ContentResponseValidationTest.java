import org.junit.Before;
import org.junit.Test;
import service.error.InvalidContent;
import service.error.WrongContentType;
import service.error.WrongResponseCode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static service.Loader.validateContentResponse;

/**
 * Created by igor on 20.11.2016.
 */
public class ContentResponseValidationTest {

    private static final String WRONG = "WRONG";

    private int statusCode;
    private String contentType;
    private String content;

    @Before
    public void setUp() throws Exception {
        this.statusCode = 200;
        this.contentType = "text/csv";
        this.content = "content";
    }

    @Test
    public void happyFlow() throws Exception {

        try {
            validateContentResponse(this.statusCode, this.contentType, this.content);
        } catch (Exception exception) {
            fail();
        }

    }

    @Test
    public void wrongStatusCode() throws Exception {

        try {
            validateContentResponse(48, this.contentType, this.content);

            fail();
        } catch (Exception exception) {
            assertEquals(new WrongResponseCode(200, 48), exception);
        }

    }

    @Test
    public void wrongContentType() throws Exception {

        try {
            validateContentResponse(this.statusCode, WRONG, this.content);

            fail();
        } catch (Exception exception) {
            assertEquals(new WrongContentType(WRONG), exception);
        }

    }

    @Test
    public void nullContent() throws Exception {

        try {
            validateContentResponse(this.statusCode, this.contentType, null);

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidContent(null), exception);
        }

    }

    @Test
    public void emptyContent() throws Exception {

        try {
            validateContentResponse(this.statusCode, this.contentType, "");

            fail();
        } catch (Exception exception) {
            assertEquals(new InvalidContent(""), exception);
        }

    }

}
