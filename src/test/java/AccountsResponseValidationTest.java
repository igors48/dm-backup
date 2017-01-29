import org.junit.Test;
import service.Loader;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by igor on 28.01.2017.
 */
public class AccountsResponseValidationTest {

    @Test
    public void smoke() throws Exception {
        assertTrue(Loader.validateAccountsResponse(200, "text/json; charset=utf-8"));
        assertFalse(Loader.validateAccountsResponse(300, "text/json; charset=utf-8"));
        assertFalse(Loader.validateAccountsResponse(200, "text/json;"));
        assertFalse(Loader.validateAccountsResponse(300, "text/json;"));
    }

}
