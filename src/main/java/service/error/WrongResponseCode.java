package service.error;

/**
 * Created by igor on 16.11.2016.
 */
public class WrongResponseCode extends ServiceException {

    private final int expected;
    private final int actual;

    public WrongResponseCode(final int expected, final int actual) {
        this.expected = expected;
        this.actual = actual;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WrongResponseCode that = (WrongResponseCode) o;

        if (expected != that.expected) return false;

        return actual == that.actual;
    }

    @Override
    public int hashCode() {
        int result = expected;
        result = 31 * result + actual;

        return result;
    }

    @Override
    public String toString() {
        return "WrongResponseCode{" +
                "expected=" + expected +
                ", actual=" + actual +
                '}';
    }

}
