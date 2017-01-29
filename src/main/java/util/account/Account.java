package util.account;

import static util.Assert.guard;
import static util.Parameter.isValidString;

/**
 * Created by igor on 28.01.2017.
 */
public class Account {

    public final String title;
    public final String balance;

    public Account(final String title, final String balance) {
        guard(isValidString(this.title = title));
        guard(isValidString(this.balance = balance));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (!title.equals(account.title)) return false;
        return balance.equals(account.balance);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + balance.hashCode();
        return result;
    }

}
