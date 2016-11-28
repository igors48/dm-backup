package gae;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import service.TimestampRepository;

import java.util.HashMap;
import java.util.Map;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 28.11.2016.
 */
public class GaeTimestampRepository<Long> extends AbstractRepository implements TimestampRepository {

    public static final int VERSION = 1;
    public static final String TIMESTAMP_KEY = "timestamp";

    public class Writer implements gae.Writer<Long> {

        @Override
        public void write(final Entity entity, final Long data) {
            guard(notNull(data));

            entity.setProperty(TIMESTAMP_KEY, data);
        }

    }

    public class Reader implements gae.Reader<Long> {

        @Override
        public Long read(final Entity entity) {
            guard(notNull(entity));

            return (Long) entity.getProperty(TIMESTAMP_KEY);
        }

    }

    @Override
    public void store(final long timestamp) {
        final Key key = GaeDatastoreTools.getEntityRootKey(Kind.TIMESTAMP.value, Kind.TIMESTAMP);
        final Entity entity = new Entity(Kind.TIMESTAMP.value, key);

        this.writeToEntity(entity, timestamp);
    }

    @Override
    public Long load() {
        return null;
    }

    @Override
    public void clear() {

    }

    private GaeTimestampRepository(final int version, final Map<Integer, gae.Reader> readers, final gae.Writer writer) {
        super(version, readers, writer);
    }

    public static GaeTimestampRepository create() {
        final Map<Integer, gae.Reader> readers = new HashMap<>();
        readers.put(1, new Reader());

        return new GaeTimestampRepository(VERSION, readers, new Writer());
    }

}
