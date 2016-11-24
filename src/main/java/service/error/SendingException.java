package service.error;

/**
 * Created by igor on 16.11.2016.
 */
public class SendingException extends ServiceException {

    private final String message;

    public SendingException(final String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SendingException{" +
                "message='" + message + '\'' +
                '}';
    }

}
