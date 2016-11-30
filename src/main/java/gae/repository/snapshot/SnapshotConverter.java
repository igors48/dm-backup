package gae.repository.snapshot;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import gae.repository.AbstractConverter;
import gae.repository.GaeDatastoreTools;
import gae.repository.Kind;
import service.Snapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 30.11.2016.
 */
public class SnapshotConverter extends AbstractConverter<Snapshot> {

    public static final SnapshotConverter SNAPSHOT_CONVERTER = SnapshotConverter.create();

    public static final int VERSION = 1;

    public static final String UUID_KEY = "uuid";
    public static final String TIMESTAMP_KEY = "timestamp";
    public static final String CONTENT_KEY = "content";

    private static class Writer implements gae.repository.Writer<Snapshot> {

        @Override
        public void write(final Entity entity, final Snapshot data) {
            guard(notNull(data));

            entity.setProperty(UUID_KEY, data.uuid);
            entity.setProperty(TIMESTAMP_KEY, data.timestamp);
            entity.setProperty(CONTENT_KEY, data.content);
        }

    }

    private static class Reader implements gae.repository.Reader<Snapshot> {

        @Override
        public Snapshot read(final Entity entity) {
            guard(notNull(entity));

            final UUID uuid = (UUID) entity.getProperty(UUID_KEY);
            final long timestamp = (long) entity.getProperty(TIMESTAMP_KEY);
            final String content = (String) entity.getProperty(CONTENT_KEY);

            return new Snapshot(uuid, timestamp, content);
        }

    }

    private SnapshotConverter(final int version, final Map<Integer, gae.repository.Reader<Snapshot>> readers, final gae.repository.Writer<Snapshot> writer) {
        super(version, readers, writer);
    }

    @Override
    protected Entity createEmptyEntityFor(final Snapshot data) {
        final Key key = GaeDatastoreTools.createEntityKey(data.uuid.toString(), Kind.SNAPSHOT);

        return new Entity(Kind.SNAPSHOT.value, key);
    }

    private static SnapshotConverter create() {
        final Map<Integer, gae.repository.Reader<Snapshot>> readers = new HashMap<>();
        readers.put(VERSION, new Reader());

        return new SnapshotConverter(VERSION, readers, new Writer());
    }

}
