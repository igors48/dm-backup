package service;

/**
 * Created by igor on 25.11.2016.
 */
public class ChangesTimestamp {

    public final long timestamp;
    public final boolean notificationSent;

    public ChangesTimestamp(final long timestamp, final boolean notificationSent) {
        this.timestamp = timestamp;
        this.notificationSent = notificationSent;
    }

}
