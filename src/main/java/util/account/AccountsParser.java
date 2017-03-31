package util.account;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static util.Assert.guard;
import static util.Parameter.isValidString;
import static util.Parameter.notNull;

/**
 * Created by igor on 28.01.2017.
 */
public class AccountsParser {

    private static final Logger LOGGER = Logger.getLogger(AccountsParser.class.getName());

    public static final String TITLE = "title";
    public static final String BALANCE = "balance";

    public static List<Account> parse(final String data) {
        guard(isValidString(data));

        final List<Account> result = new ArrayList<>();

        try {
            final ObjectMapper mapper = new ObjectMapper();
            final TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
            };

            final HashMap<String, Object> map = mapper.readValue(data, typeRef);
            final Map<String, Map<String, Object>> accounts = (Map<String, Map<String, Object>>) map.get("account");

            for (Map<String, Object> current : accounts.values()) {
                final Account account = parse(current);

                if (account != null) {
                    result.add(account);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while parsing accounts", e);
        }

        return result;
    }

    public static Account parse(final Map<String, Object> data) {
        guard(notNull(data));

        final String title = (String) data.get(TITLE);
        final String balance = (String) data.get(BALANCE);

        final boolean isValid = isValidString(title) && isValidString(balance);

        LOGGER.log(Level.SEVERE, format("Invalid account [ %s ] [ %s ]", title, balance));

        return isValid ? new Account(title, balance) : null;
    }

}
