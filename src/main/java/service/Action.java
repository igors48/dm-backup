package service;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 03.12.2016.
 */
public class Action {

    public final Type type;

    public Action(final Type type) {
        guard(notNull(this.type = type));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Action action = (Action) o;

        return type == action.type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    public enum Type {
        NO_ACTION,
        SAVE,
        UPDATE_LAST,
        SEND
    }

}
