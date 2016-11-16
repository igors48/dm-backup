package service.error;

import service.ServiceError;

/**
 * Created by igor on 16.11.2016.
 */
public class InvalidSessionId extends ServiceError {

    public final String sessionId;

    public InvalidSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }
}
