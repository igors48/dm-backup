package service;

import java.util.UUID;

import static util.Assert.guard;
import static util.Parameter.*;

/**
 * Created by igor on 29.11.2016.
 */
public class Snapshot {

    public final UUID uuid;
    public final long timestamp;
    public final String content;

    public Snapshot(final UUID uuid, final long timestamp, final String content) {
        guard(notNull(this.uuid = uuid));
        guard(isPositive(this.timestamp = timestamp));
        guard(isValidString(this.content = content));
    }

}
