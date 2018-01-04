package gae.repository.timestamp;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import gae.repository.Converter;
import gae.repository.EntityFactory;
import gae.repository.Kind;
import gae.repository.Repository;

import java.util.HashMap;
import java.util.Map;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 30.11.2016.
 */
public class TimestampConverter extends Converter<Long> {

    public static final TimestampConverter TIMESTAMP_CONVERTER = TimestampConverter.create();

    private static final int VERSION = 1;
    private static final String TIMESTAMP_KEY = "timestamp";

    private static class Writer implements gae.repository.Writer<Long> {

        @Override
        public void write(final Entity entity, final Long data) {
            guard(notNull(entity));
            guard(notNull(data));

            entity.setProperty(TIMESTAMP_KEY, data);

            addReadableTimeStamp(data, entity);
        }

    }

    private static class Reader implements gae.repository.Reader<Long> {

        @Override
        public Long read(final Entity entity) {
            guard(notNull(entity));

            return (Long) entity.getProperty(TIMESTAMP_KEY);
        }

    }

    private TimestampConverter(final int version, final Map<Long, gae.repository.Reader<Long>> readers, final gae.repository.Writer<Long> writer, final EntityFactory<Long> entityFactory) {
        super(version, Kind.TIMESTAMP, readers, writer, entityFactory);
    }

    private static TimestampConverter create() {
        final EntityFactory<Long> entityFactory = new EntityFactory<Long>() {
            @Override
            public Entity createFor(final Long data) {
                final Key key = Repository.createEntityKey(Kind.TIMESTAMP.value, Kind.TIMESTAMP);

                return new Entity(Kind.TIMESTAMP.value, key);
            }
        };

        return create(entityFactory);
    }

    public static TimestampConverter create(final EntityFactory<Long> entityFactory) {
        guard(notNull(entityFactory));

        final Map<Long, gae.repository.Reader<Long>> readers = new HashMap<>();
        readers.put(1L, new Reader());

        return new TimestampConverter(VERSION, readers, new Writer(), entityFactory);
    }

}
