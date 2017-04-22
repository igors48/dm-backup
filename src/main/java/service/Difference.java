package service;

import static util.Assert.guard;
import static util.Parameter.isValidString;
import static util.Parameter.notNull;

/**
 * Created by igor on 21.04.2017.
 */
public class Difference {

    public final String title;
    public final String before;
    public final String after;
    public final String delta;
    public final String color;

    public Difference(final String title, final String before, final String after, final String delta, final String color) {
        guard(isValidString(this.title = title));
        guard(notNull(this.before = before));
        guard(notNull(this.after = after));
        guard(notNull(this.delta = delta));
        guard(notNull(this.color = color));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Difference that = (Difference) o;

        if (!title.equals(that.title)) return false;
        if (!before.equals(that.before)) return false;
        if (!after.equals(that.after)) return false;
        if (!delta.equals(that.delta)) return false;
        return color.equals(that.color);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + before.hashCode();
        result = 31 * result + after.hashCode();
        result = 31 * result + delta.hashCode();
        result = 31 * result + color.hashCode();
        return result;
    }
}
