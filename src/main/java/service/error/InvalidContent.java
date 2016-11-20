package service.error;

import service.ServiceException;

/**
 * Created by igor on 16.11.2016.
 */
public class InvalidContent extends ServiceException {

    public final String content;

    public InvalidContent(final String content) {
        this.content = content;
    }

}
