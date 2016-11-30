package gae.repository;

import com.google.appengine.api.datastore.Entity;

import java.util.Map;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 27.11.2016.
 */
public abstract class AbstractConverter<T> {

    public static final String VERSION_KEY = "_version_";

    private final int version;
    private final Map<Integer, Reader<T>> readers;
    private final Writer<T> writer;

    public AbstractConverter(final int version, final Map<Integer, Reader<T>> readers, final Writer<T> writer) {
        this.version = version;
        guard(validReaders(this.readers = readers));
        guard(notNull(this.writer = writer));
    }

    protected Entity convert(final T data) {
        final Entity entity = this.createEmptyEntityFor(data);

        this.writer.write(entity, data);
        entity.setProperty(VERSION_KEY, this.version);

        return entity;
    }

    protected T convert(final Entity entity) {
        final Integer version = (Integer) entity.getProperty(VERSION_KEY);
        final Reader<T> reader = this.readers.get(version);

        return reader.read(entity);
    }

    protected abstract Entity createEmptyEntityFor(final T data);

    private static boolean validReaders(final Map readers) {
        return readers != null && !readers.isEmpty();
    }

}
