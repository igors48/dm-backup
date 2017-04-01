package util.account;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 31.03.2017.
 */
public class ParsedAccount {

    private static final Logger LOGGER = Logger.getLogger(ParsedAccount.class.getName());

    public final String title;
    public final Float balance;

    public ParsedAccount(final String title, final Float balance) {
        guard(notNull(this.title = title));
        this.balance = balance;
    }

    public static ParsedAccount create(final Account account) {
        guard(notNull(account));

        Float balance = null;

        try {
            balance = Float.valueOf(account.balance);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, format("Float value can not be parsed from [ %s ]", account.balance), e);
        }

        return new ParsedAccount(account.title, balance);
    }

}
