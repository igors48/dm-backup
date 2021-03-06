package util.account;

import static util.Assert.guard;
import static util.Parameter.isValidString;
import static util.Parameter.notNull;

/**
 * Created by igor on 31.03.2017.
 */
public class ComparisonResult implements Comparable<ComparisonResult> {

    public final String title;
    public final Float before;
    public final Float after;
    public final Float delta;

    public ComparisonResult(final String title, final Float before, final Float after, final Float delta) {
        guard(isValidString(this.title = title));
        this.before = before;
        this.after = after;
        guard(notNull(this.delta = delta));
    }

    @Override
    public int compareTo(final ComparisonResult other) {
        return this.title.compareTo(other.title);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComparisonResult that = (ComparisonResult) o;

        if (!title.equals(that.title)) return false;
        if (before != null ? !before.equals(that.before) : that.before != null) return false;
        if (after != null ? !after.equals(that.after) : that.after != null) return false;
        return delta != null ? delta.equals(that.delta) : that.delta == null;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + (before != null ? before.hashCode() : 0);
        result = 31 * result + (after != null ? after.hashCode() : 0);
        result = 31 * result + (delta != null ? delta.hashCode() : 0);
        return result;
    }
}
