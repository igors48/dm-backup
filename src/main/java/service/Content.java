package service;

import util.account.Account;

import java.util.List;

import static util.Assert.guard;
import static util.Parameter.isValidString;
import static util.Parameter.notNull;

/**
 * Created by igor on 29.01.2017.
 */
public class Content {

    public List<Account> accounts;
    public String file;

    private Content(){}

    public Content(final List<Account> accounts, final String file) {
        guard(notNull(this.accounts = accounts));
        guard(isValidString(this.file = file));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Content content = (Content) o;

        if (!accounts.equals(content.accounts)) return false;
        return file.equals(content.file);
    }

    @Override
    public int hashCode() {
        int result = accounts.hashCode();
        result = 31 * result + file.hashCode();
        return result;
    }

}
