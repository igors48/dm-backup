package service.error;

import service.ServiceError;

/**
 * Created by igor on 16.11.2016.
 */
public class IoError extends ServiceError {

    public String message;

    public IoError(final String message) {
        this.message = message;
    }

}
