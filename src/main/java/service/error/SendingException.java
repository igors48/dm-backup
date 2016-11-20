package service.error;

import service.ServiceException;

/**
 * Created by igor on 16.11.2016.
 */
public class SendingException extends ServiceException {

    public String message;

    public SendingException(final String message) {
        this.message = message;
    }

}
