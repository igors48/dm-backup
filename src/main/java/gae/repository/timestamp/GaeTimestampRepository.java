package gae.repository.timestamp;

import com.google.appengine.api.datastore.Entity;
import gae.repository.Converter;
import gae.repository.GaeDatastore;
import gae.repository.GaeDatastoreTools;
import service.TimestampRepository;

import java.util.List;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 28.11.2016.
 */
public class GaeTimestampRepository implements TimestampRepository {

    private final Converter<Long> converter;

    public GaeTimestampRepository(final Converter<Long> converter) {
        guard(notNull(this.converter = converter));
    }

    @Override
    public void store(final long timestamp) {
        final Entity entity = this.converter.convert(timestamp);
        GaeDatastore.INSTANCE.getDatastoreService().put(entity);
    }

    @Override
    public Long load() {
        final List<Entity> entities = GaeDatastoreTools.loadEntities(this.converter.getKind());

        return entities.isEmpty() ? null : this.converter.convert(entities.get(0));
    }

    @Override
    public void clear() {
        final List<Entity> victims = GaeDatastoreTools.loadEntities(this.converter.getKind());

        for (final Entity victim : victims) {
            GaeDatastore.INSTANCE.getDatastoreService().delete(victim.getKey());
        }
    }

}
