package service.error;

import service.ServiceException;

/**
 * Created by igor on 16.11.2016.
 */
public class InvalidSessionId extends ServiceException {

    public final String sessionId;

    public InvalidSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }
}
