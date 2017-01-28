import org.junit.Test;
import service.Loader;

import static org.junit.Assert.assertFalse;

/**
 * Created by igor on 28.01.2017.
 */
public class AccountsResponseValidationTest {

    @Test
    public void smoke() throws Exception {
        assertFalse(Loader.validateAccountsResponse(200, ""));

    }
}
