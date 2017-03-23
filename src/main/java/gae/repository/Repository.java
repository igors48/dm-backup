package gae.repository;

import com.google.appengine.api.datastore.Entity;

import java.util.ArrayList;
import java.util.List;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 23.03.2017.
 */
public class Repository<T> {

    //TODO tests

    private final Converter<T> converter;

    public Repository(final Converter<T> converter) {
        guard(notNull(this.converter = converter));
    }

    public List<T> loadAll() {
        final List<T> result = new ArrayList<>();

        final List<Entity> entities = GaeDatastoreTools.loadEntities(this.converter.getKind());

        for (final Entity entity : entities) {
            final T snapshot = this.converter.convert(entity);
            result.add(snapshot);
        }

        return result;
    }

    public void storeAll(final List<T> values) {
        guard(notNull(values));

        this.clear();

        for (final T snapshot : values) {
            final Entity entity = this.converter.convert(snapshot);
            GaeDatastore.INSTANCE.getDatastoreService().put(entity);
        }
    }

    public void store(final T value) {
        final Entity entity = this.converter.convert(value);
        GaeDatastore.INSTANCE.getDatastoreService().put(entity);
    }

    public void clear() {
        final List<Entity> entities = GaeDatastoreTools.loadEntities(this.converter.getKind());

        for (final Entity entity : entities) {
            GaeDatastore.INSTANCE.getDatastoreService().delete(entity.getKey());
        }
    }
}
