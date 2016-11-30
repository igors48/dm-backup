package gae.repository.timestamp;

import com.google.appengine.api.datastore.Entity;
import gae.repository.AbstractConverter;
import gae.repository.GaeDatastore;
import gae.repository.GaeDatastoreTools;
import gae.repository.Kind;
import service.TimestampRepository;

import java.util.List;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 28.11.2016.
 */
public class GaeTimestampRepository implements TimestampRepository {

    private final AbstractConverter<Long> converter;

    public GaeTimestampRepository(final AbstractConverter<Long> converter) {
        guard(notNull(this.converter = converter));
    }

    @Override
    public void store(final long timestamp) {
        this.converter.convert(timestamp);
    }

    @Override
    public Long load() {
        final Entity entity = GaeDatastoreTools.loadEntity(Kind.TIMESTAMP.value, Kind.TIMESTAMP, false);

        return this.converter.convert(entity);
    }

    @Override
    public void clear() {
        final List<Entity> victims = GaeDatastoreTools.loadEntities(Kind.TIMESTAMP);

        for (final Entity victim : victims) {
            GaeDatastore.INSTANCE.getDatastoreService().delete(victim.getKey());
        }
    }

}
