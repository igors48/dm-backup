package service.error;

import service.ServiceError;

/**
 * Created by igor on 16.11.2016.
 */
public class InvalidContent extends ServiceError {

    public final String content;

    public InvalidContent(final String content) {
        this.content = content;
    }

}
