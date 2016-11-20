package service.error;

import service.ServiceException;

/**
 * Created by igor on 16.11.2016.
 */
public class WrongContentType extends ServiceException {

    private final String actual;

    public WrongContentType(final String actual) {
        this.actual = actual;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WrongContentType that = (WrongContentType) o;

        return actual != null ? actual.equals(that.actual) : that.actual == null;
    }

    @Override
    public int hashCode() {
        return actual != null ? actual.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "WrongContentType{" +
                "actual='" + actual + '\'' +
                '}';
    }
}
