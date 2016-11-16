package service.error;

import service.ServiceError;

/**
 * Created by igor on 16.11.2016.
 */
public class WrongContentType extends ServiceError {

    public final String actual;

    public WrongContentType(final String actual) {
        this.actual = actual;
    }

}
