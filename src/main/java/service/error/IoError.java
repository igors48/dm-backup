package service.error;

/**
 * Created by igor on 16.11.2016.
 */
public class IoError extends ServiceException {

    private final String message;

    public IoError(final String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "IoError{" +
                "message='" + message + '\'' +
                '}';
    }

}
