package gae;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import service.Snapshot;
import service.SnapshotRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 29.11.2016.
 */
public class GaeSnapshotRepository extends AbstractRepository<Snapshot> implements SnapshotRepository {

    public static final int VERSION = 1;

    public static final String UUID_KEY = "uuid";
    public static final String TIMESTAMP_KEY = "timestamp";
    public static final String CONTENT_KEY = "content";

    public static class Writer implements gae.Writer<Snapshot> {

        @Override
        public void write(final Entity entity, final Snapshot data) {
            guard(notNull(data));

            entity.setProperty(UUID_KEY, data.uuid);
            entity.setProperty(TIMESTAMP_KEY, data.timestamp);
            entity.setProperty(CONTENT_KEY, data.content);
        }

    }

    public static class Reader implements gae.Reader<Snapshot> {

        @Override
        public Snapshot read(final Entity entity) {
            guard(notNull(entity));

            final UUID uuid = (UUID) entity.getProperty(UUID_KEY);
            final long timestamp = (long) entity.getProperty(TIMESTAMP_KEY);
            final String content = (String) entity.getProperty(CONTENT_KEY);

            return new Snapshot(uuid, timestamp, content);
        }

    }

    @Override
    public String loadLatestSnapshot() {
        final List<Entity> entities = GaeDatastoreTools.loadEntities(Kind.SNAPSHOT);

        Snapshot latest = null;

        for (final Entity entity : entities) {
            final Snapshot snapshot = this.read(entity);

            if ((latest == null) || (snapshot.timestamp > latest.timestamp)) {
                latest = snapshot;
            }
        }

        return latest == null ? "" : latest.content;
    }

    @Override
    protected Entity createEmptyEntityFor(final Snapshot snapshot) {
        final Key key = GaeDatastoreTools.createEntityKey(snapshot.uuid.toString(), Kind.SNAPSHOT);

        return new Entity(Kind.SNAPSHOT.value, key);
    }

    private GaeSnapshotRepository(final int version, final Map<Integer, gae.Reader<Snapshot>> readers, final gae.Writer<Snapshot> writer) {
        super(version, readers, writer);
    }

    public static GaeSnapshotRepository create() {
        final Map<Integer, gae.Reader<Snapshot>> readers = new HashMap<>();
        readers.put(VERSION, new Reader());

        return new GaeSnapshotRepository(VERSION, readers, new Writer());
    }

}
