package util.account;

import static util.Assert.guard;
import static util.Parameter.isValidString;

/**
 * Created by igor on 31.03.2017.
 */
public class ComparisonResult implements Comparable<ComparisonResult> {

    public final String title;
    public final Float before;
    public final Float after;

    public ComparisonResult(final String title, final Float before, final Float after) {
        guard(isValidString(this.title = title));
        this.before = before;
        this.after = after;
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
        return after != null ? after.equals(that.after) : that.after == null;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + (before != null ? before.hashCode() : 0);
        result = 31 * result + (after != null ? after.hashCode() : 0);
        return result;
    }

}
