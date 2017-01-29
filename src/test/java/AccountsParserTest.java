import org.junit.Test;
import util.account.Account;
import util.account.AccountsParser;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.Files.readAllBytes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by igor on 28.01.2017.
 */
public class AccountsParserTest {

    @Test
    public void smoke() throws Exception {
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("data.json").getFile());
        final String data = new String(readAllBytes(file.toPath()));

        final List<Account> result = AccountsParser.parse(data);

        assertEquals(11, result.size());
    }

    @Test
    public void mapEntryParse() throws Exception {
        final Map<String, Object> titleIsNull = new HashMap<>();
        titleIsNull.put(AccountsParser.TITLE, null);
        titleIsNull.put(AccountsParser.BALANCE, AccountsParser.BALANCE);
        assertNull(AccountsParser.parse(titleIsNull));

        final Map<String, Object> balanceIsNull = new HashMap<>();
        titleIsNull.put(AccountsParser.TITLE, AccountsParser.TITLE);
        titleIsNull.put(AccountsParser.BALANCE, null);
        assertNull(AccountsParser.parse(balanceIsNull));

        final Map<String, Object> allOk = new HashMap<>();
        allOk.put(AccountsParser.TITLE, AccountsParser.TITLE);
        allOk.put(AccountsParser.BALANCE, AccountsParser.BALANCE);
        assertEquals(new Account(AccountsParser.TITLE, AccountsParser.BALANCE), AccountsParser.parse(allOk));
    }

}
