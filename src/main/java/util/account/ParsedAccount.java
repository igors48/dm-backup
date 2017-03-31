package util.account;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 31.03.2017.
 */
public class ParsedAccount {

    public final String title;
    public final float balance;
    public final boolean valid;

    private ParsedAccount(final String title, final float balance, final boolean valid) {
        this.title = title;
        this.balance = balance;
        this.valid = valid;
    }

    public static ParsedAccount create(final Account account) {
        guard(notNull(account));

        boolean valid = true;
        float balance = 0f;

        try {
            balance = Float.valueOf(account.balance);
        } catch (Exception e) {
            valid = false;
        }

        return new ParsedAccount(account.title, balance, valid);
    }

}
