package gae;

import com.google.appengine.api.datastore.Entity;

import java.util.Map;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 27.11.2016.
 */
public abstract class AbstractRepository<T> {

    public static final String VERSION_KEY = "_version_";

    private final int version;
    private final Map<Integer, Reader<T>> readers;
    private final Writer<T> writer;

    public AbstractRepository(final int version, final Map<Integer, Reader<T>> readers, final Writer<T> writer) {
        this.version = version;
        guard(validReaders(this.readers = readers));
        guard(notNull(this.writer = writer));
    }

    protected void writeToEntity(final Entity entity, final T data) {
        this.writer.write(entity, data);
        entity.setProperty(VERSION_KEY, this.version);
    }

    protected T readFromEntity(final Entity entity) {
        final Integer version = (Integer) entity.getProperty(VERSION_KEY);
        final Reader<T> reader = this.readers.get(version);

        return reader.read(entity);
    }

    private boolean validReaders(final Map<Integer, Reader<T>> readers) {
        return readers != null && !readers.isEmpty();
    }

}
