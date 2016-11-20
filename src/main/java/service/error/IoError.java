package service.error;

import service.ServiceException;

/**
 * Created by igor on 16.11.2016.
 */
public class IoError extends ServiceException {

    public final String message;

    public IoError(final String message) {
        this.message = message;
    }

}
