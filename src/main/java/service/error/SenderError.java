package service.error;

import service.ServiceError;

/**
 * Created by igor on 16.11.2016.
 */
public class SenderError extends ServiceError {

    public String message;

    public SenderError(final String message) {
        this.message = message;
    }

}
