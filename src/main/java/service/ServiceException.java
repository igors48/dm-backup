package service;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 14.11.2016.
 */
public class ServiceException extends Exception {

    public final ServiceError error;

    public ServiceException(final ServiceError error) {
        guard(notNull(this.error = error));
    }

}
