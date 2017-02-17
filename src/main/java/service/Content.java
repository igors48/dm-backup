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

    public final List<Account> accounts;
    public final String file;

    public Content(final List<Account> accounts, final String file) {
        guard(notNull(this.accounts = accounts));
        guard(isValidString(this.file = file));
    }

}
