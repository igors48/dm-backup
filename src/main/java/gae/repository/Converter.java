package gae.repository;

import com.google.appengine.api.datastore.Entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 27.11.2016.
 */
public class Converter<T> {

    public static final String VERSION_KEY = "_version_";
    public static final String READABLE_TIMESTAMP_KEY = "readable_timestamp";

    private static final String READABLE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final long version;
    private final Kind kind;
    private final Map<Long, Reader<T>> readers;
    private final Writer<T> writer;
    private final EntityFactory<T> entityFactory;

    public Converter(final long version, final Kind kind, final Map<Long, Reader<T>> readers, final Writer<T> writer, final EntityFactory<T> entityFactory) {
        this.version = version;
        guard(notNull(this.kind = kind));
        guard(validReaders(this.readers = readers));
        guard(notNull(this.writer = writer));
        guard(notNull(this.entityFactory = entityFactory));
    }

    public Kind getKind() {
        return this.kind;
    }

    public Entity convert(final T data) {
        guard(notNull(data));

        final Entity entity = this.entityFactory.createFor(data);

        this.writer.write(entity, data);
        entity.setProperty(VERSION_KEY, this.version);

        return entity;
    }

    public T convert(final Entity entity) {

        if (entity == null) {
            return null;
        }

        final Long version = (Long) entity.getProperty(VERSION_KEY);
        final Reader<T> reader = this.readers.get(version);

        return reader.read(entity);
    }

    public static void addReadableTimeStamp(final long timestamp, final Entity entity) {
        final SimpleDateFormat readableTimestampFormat = new SimpleDateFormat(READABLE_DATE_FORMAT);
        final Date date = new Date(timestamp);
        final String readableTimeStamp = readableTimestampFormat.format(date);

        entity.setProperty(READABLE_TIMESTAMP_KEY, readableTimeStamp);
    }

    private static boolean validReaders(final Map readers) {
        return readers != null && !readers.isEmpty();
    }

}
