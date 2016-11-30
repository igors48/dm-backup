package gae.repository.timestamp;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import gae.repository.AbstractConverter;
import gae.repository.GaeDatastore;
import gae.repository.GaeDatastoreTools;
import gae.repository.Kind;
import service.TimestampRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 28.11.2016.
 */
public class GaeTimestampRepository extends AbstractConverter<Long> implements TimestampRepository {

    public static final int VERSION = 1;
    public static final String TIMESTAMP_KEY = "timestamp";

    public static class Writer implements gae.repository.Writer<Long> {

        @Override
        public void write(final Entity entity, final Long data) {
            guard(notNull(data));

            entity.setProperty(TIMESTAMP_KEY, data);
        }

    }

    public static class Reader implements gae.repository.Reader<Long> {

        @Override
        public Long read(final Entity entity) {
            guard(notNull(entity));

            return (Long) entity.getProperty(TIMESTAMP_KEY);
        }

    }

    @Override
    public void store(final long timestamp) {
        this.convert(timestamp);
    }

    @Override
    public Long load() {
        final Entity entity = GaeDatastoreTools.loadEntity(Kind.TIMESTAMP.value, Kind.TIMESTAMP, false);

        return this.convert(entity);
    }

    @Override
    public void clear() {
        final List<Entity> victims = GaeDatastoreTools.loadEntities(Kind.TIMESTAMP);

        for (final Entity victim : victims) {
            GaeDatastore.INSTANCE.getDatastoreService().delete(victim.getKey());
        }
    }

    @Override
    protected Entity createEmptyEntityFor(final Long data) {
        final Key key = GaeDatastoreTools.createEntityKey(Kind.TIMESTAMP.value, Kind.TIMESTAMP);

        return new Entity(Kind.TIMESTAMP.value, key);
    }

    private GaeTimestampRepository(final int version, final Map<Integer, gae.repository.Reader<Long>> readers, final gae.repository.Writer<Long> writer) {
        super(version, readers, writer);
    }

    public static GaeTimestampRepository create() {
        final Map<Integer, gae.repository.Reader<Long>> readers = new HashMap<>();
        readers.put(1, new Reader());

        return new GaeTimestampRepository(VERSION, readers, new Writer());
    }

}
