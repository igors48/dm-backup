package service.error;

/**
 * Created by igor on 16.11.2016.
 */
public class InvalidSessionId extends ServiceException {

    private final String sessionId;

    public InvalidSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvalidSessionId that = (InvalidSessionId) o;

        return sessionId != null ? sessionId.equals(that.sessionId) : that.sessionId == null;
    }

    @Override
    public int hashCode() {
        return sessionId != null ? sessionId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "InvalidSessionId{" +
                "sessionId='" + sessionId + '\'' +
                '}';
    }

}
