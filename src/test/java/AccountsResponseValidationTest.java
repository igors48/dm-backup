import org.junit.Test;
import service.Loader;

import static org.junit.Assert.assertTrue;

/**
 * Created by igor on 28.01.2017.
 */
public class AccountsResponseValidationTest {

    @Test
    public void smoke() throws Exception {
        assertTrue(Loader.validateAccountsResponse(200, ""));

    }
}
