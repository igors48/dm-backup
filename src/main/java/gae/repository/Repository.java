package gae.repository;

import com.google.appengine.api.datastore.*;

import java.util.ArrayList;
import java.util.List;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static java.lang.Integer.MAX_VALUE;
import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 23.03.2017.
 */
public class Repository<T> {

    //TODO tests
    //TODO consider inject KeyFactory

    private static final Key ROOT_KEY = KeyFactory.createKey(Kind.ROOT.value, Kind.ROOT.value);

    private final DatastoreService service;
    private final Converter<T> converter;

    public Repository(final DatastoreService service, final Converter<T> converter) {
        guard(notNull(this.service = service));
        guard(notNull(this.converter = converter));
    }

    public List<T> loadAll() {
        final List<T> result = new ArrayList<>();

        final List<Entity> entities = this.loadEntities(this.converter.getKind());

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
            this.service.put(entity);
        }
    }

    public void store(final T value) {
        final Entity entity = this.converter.convert(value);
        this.service.put(entity);
    }

    public void clear() {
        final List<Entity> entities = this.loadEntities(this.converter.getKind());

        for (final Entity entity : entities) {
            this.service.delete(entity.getKey());
        }
    }

    private List<Entity> loadEntities(final Kind kind) {
        final Query query = new Query(kind.value);
        final PreparedQuery preparedQuery = this.service.prepare(query);

        return preparedQuery.asList(withLimit(MAX_VALUE));
    }

    public static Key createEntityKey(final String uuid, final Kind kind) {
        guard(notNull(uuid));
        guard(notNull(kind));

        return KeyFactory.createKey(ROOT_KEY, kind.value, uuid);
    }

}
