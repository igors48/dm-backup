package service;

import java.util.Comparator;
import java.util.UUID;

import static util.Assert.guard;
import static util.Parameter.isPositive;
import static util.Parameter.notNull;

/**
 * Created by igor on 29.11.2016.
 */
public class Snapshot {

    public static final SnapshotTimestampComparator TIMESTAMP_COMPARATOR = new SnapshotTimestampComparator();

    public UUID uuid;
    public long timestamp;
    public Content content;

    public Snapshot(final UUID uuid, final long timestamp, final Content content) {
        guard(notNull(this.uuid = uuid));
        guard(isPositive(this.timestamp = timestamp));
        guard(notNull(this.content = content));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Snapshot snapshot = (Snapshot) o;

        if (timestamp != snapshot.timestamp) return false;
        if (!uuid.equals(snapshot.uuid)) return false;
        return content.equals(snapshot.content);
    }

    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + content.hashCode();
        return result;
    }

    private static class SnapshotTimestampComparator implements Comparator<Snapshot> {

        @Override
        public int compare(Snapshot first, Snapshot second) {
            final long firstTimestamp = first.timestamp;
            final long secondTimestamp = second.timestamp;

            if (firstTimestamp == secondTimestamp) {
                return 0;
            }

            return firstTimestamp > secondTimestamp ? 1 : -1;
        }
    }

}
