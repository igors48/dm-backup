package gae.repository.timestamp;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import gae.repository.AbstractConverter;
import gae.repository.GaeDatastoreTools;
import gae.repository.Kind;

import java.util.HashMap;
import java.util.Map;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 30.11.2016.
 */
public class TimestampConverter extends AbstractConverter<Long> {

    public static final TimestampConverter TIMESTAMP_CONVERTER = TimestampConverter.create();

    private static final int VERSION = 1;
    private static final String TIMESTAMP_KEY = "timestamp";

    private static class Writer implements gae.repository.Writer<Long> {

        @Override
        public void write(final Entity entity, final Long data) {
            guard(notNull(data));

            entity.setProperty(TIMESTAMP_KEY, data);
        }

    }

    private static class Reader implements gae.repository.Reader<Long> {

        @Override
        public Long read(final Entity entity) {
            guard(notNull(entity));

            return (Long) entity.getProperty(TIMESTAMP_KEY);
        }

    }

    private TimestampConverter(final int version, final Map<Integer, gae.repository.Reader<Long>> readers, final gae.repository.Writer<Long> writer) {
        super(version, readers, writer);
    }

    @Override
    protected Entity createEmptyEntityFor(final Long data) {
        final Key key = GaeDatastoreTools.createEntityKey(Kind.TIMESTAMP.value, Kind.TIMESTAMP);

        return new Entity(Kind.TIMESTAMP.value, key);
    }

    private static TimestampConverter create() {
        final Map<Integer, gae.repository.Reader<Long>> readers = new HashMap<>();
        readers.put(1, new Reader());

        return new TimestampConverter(VERSION, readers, new Writer());
    }

}
