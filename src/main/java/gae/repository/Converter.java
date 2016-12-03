package gae.repository;

import com.google.appengine.api.datastore.Entity;

import java.util.Map;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 27.11.2016.
 */
public class Converter<T> {

    public static final String VERSION_KEY = "_version_";

    private final int version;
    private final Map<Integer, Reader<T>> readers;
    private final Writer<T> writer;
    private final EntityFactory<T> entityFactory;

    public Converter(final int version, final Map<Integer, Reader<T>> readers, final Writer<T> writer, final EntityFactory<T> entityFactory) {
        this.version = version;
        guard(validReaders(this.readers = readers));
        guard(notNull(this.writer = writer));
        guard(notNull(this.entityFactory = entityFactory));
    }

    public Entity convert(final T data) {
        guard(notNull(data));

        final Entity entity = this.entityFactory.createFor(data);

        this.writer.write(entity, data);
        entity.setProperty(VERSION_KEY, this.version);

        return entity;
    }

    public T convert(final Entity entity) {
        guard(notNull(entity));

        final Integer version = (Integer) entity.getProperty(VERSION_KEY);
        final Reader<T> reader = this.readers.get(version);

        return reader.read(entity);
    }

    private static boolean validReaders(final Map readers) {
        return readers != null && !readers.isEmpty();
    }

}
