package service;

import util.account.Account;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 03.12.2016.
 */
public class Action {

    public static final Action NO_ACTION = new Action(Type.NO_ACTION, new ArrayList<Account>(), new Date(), new Date());
    public static final Action SAVE = new Action(Type.SAVE, new ArrayList<Account>(), new Date(), new Date());
    public static final Action UPDATE_LAST = new Action(Type.UPDATE_LAST, new ArrayList<Account>(), new Date(), new Date());

    public final Type type;
    public final List<Account> accounts;
    public final Date before;
    public final Date after;

    private Action(final Type type, final List<Account> accounts, final Date before, final Date after) {
        guard(notNull(this.type = type));
        guard(notNull(this.accounts = accounts));
        guard(notNull(this.before = before));
        guard(notNull(this.after = after));
    }

    public static Action send(final List<Account> accounts, final Date before, final Date after) {
        guard(notNull(accounts));
        guard(notNull(before));
        guard(notNull(after));

        return new Action(Type.SEND, accounts, before, after);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Action action = (Action) o;

        if (type != action.type) return false;
        if (!accounts.equals(action.accounts)) return false;
        if (!before.equals(action.before)) return false;
        return after.equals(action.after);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + accounts.hashCode();
        result = 31 * result + before.hashCode();
        result = 31 * result + after.hashCode();
        return result;
    }

    public enum Type {
        NO_ACTION,
        SAVE,
        UPDATE_LAST,
        SEND
    }

}
