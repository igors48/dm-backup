package service.error;

import service.ServiceError;

/**
 * Created by igor on 16.11.2016.
 */
public class WrongResponseCode extends ServiceError {

    public final int expected;
    public final int actual;

    public WrongResponseCode(final int expected, final int actual) {
        this.expected = expected;
        this.actual = actual;
    }

}
