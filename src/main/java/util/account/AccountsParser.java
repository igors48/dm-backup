package util.account;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;
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

    public static List<ComparisonResult> compare(final List<ParsedAccount> before, final List<ParsedAccount> after) {
        guard(notNull(before));
        guard(notNull(after));

        final List<ParsedAccount> afterCopy = new ArrayList<>(after);

        final List<ComparisonResult> result = new ArrayList<>();

        for (final ParsedAccount oldAccount : before) {
            final String title = oldAccount.title;
            final ParsedAccount newAccount = find(title, afterCopy);

            if (newAccount == null) {
                final ComparisonResult removedItem = new ComparisonResult(title, oldAccount.balance, null);
                result.add(removedItem);
            } else {
                final ComparisonResult updatedItem = new ComparisonResult(title, oldAccount.balance, newAccount.balance);
                result.add(updatedItem);

                afterCopy.remove(newAccount);
            }
        }

        for (final ParsedAccount remain : afterCopy) {
            final ComparisonResult addedItem = new ComparisonResult(remain.title, null, remain.balance);
            result.add(addedItem);
        }

        Collections.sort(result);

        return result;
    }

    private static ParsedAccount find(final String title, final List<ParsedAccount> accounts) {

        for (final ParsedAccount account : accounts) {

            if (title.equals(account.title)) {
                return account;
            }
        }

        return null;
    }
}
