package service;

import util.account.Account;

import java.util.ArrayList;
import java.util.List;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 03.12.2016.
 */
public class Action {

    public static final Action NO_ACTION = new Action(Type.NO_ACTION, new ArrayList<Account>());
    public static final Action SAVE = new Action(Type.SAVE, new ArrayList<Account>());
    public static final Action UPDATE_LAST = new Action(Type.UPDATE_LAST, new ArrayList<Account>());

    public final Type type;
    public final List<Account> accounts;

    private Action(final Type type, final List<Account> accounts) {
        guard(notNull(this.type = type));
        guard(notNull(this.accounts = accounts));
    }

    public static Action send(final List<Account> accounts) {
        guard(notNull(accounts));

        return new Action(Type.SEND, accounts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Action action = (Action) o;

        if (type != action.type) return false;
        return accounts.equals(action.accounts);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + accounts.hashCode();
        return result;
    }

    public enum Type {
        NO_ACTION,
        SAVE,
        UPDATE_LAST,
        SEND
    }

}
