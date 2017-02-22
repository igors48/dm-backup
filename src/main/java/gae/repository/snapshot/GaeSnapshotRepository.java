package gae.repository.snapshot;

import com.google.appengine.api.datastore.Entity;
import gae.repository.Converter;
import gae.repository.GaeDatastore;
import gae.repository.GaeDatastoreTools;
import gae.repository.Kind;
import service.Snapshot;
import service.SnapshotRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 29.11.2016.
 */
public class GaeSnapshotRepository implements SnapshotRepository {

    private final Converter<Snapshot> converter;

    public GaeSnapshotRepository(final Converter<Snapshot> converter) {
        guard(notNull(this.converter = converter));
    }

    @Override
    public Snapshot loadLatestSnapshot() {
        final List<Snapshot> snapshots = this.loadAll();

        final int count = snapshots.size();

        return count == 0 ? null : snapshots.get(count - 1);
    }

    @Override
    public List<Snapshot> loadAll() {
        final List<Snapshot> result = new ArrayList<>();

        final List<Entity> entities = GaeDatastoreTools.loadEntities(Kind.SNAPSHOT);

        for (final Entity entity : entities) {
            final Snapshot snapshot = this.converter.convert(entity);
            result.add(snapshot);
        }

        Collections.sort(result, Snapshot.TIMESTAMP_COMPARATOR);

        return result;
    }

    @Override
    public void storeAll(final List<Snapshot> snapshots) {
        guard(notNull(snapshots));
        this.clear();

        for (final Snapshot snapshot : snapshots) {
            final Entity entity = this.converter.convert(snapshot);
            GaeDatastore.INSTANCE.getDatastoreService().put(entity);
        }
    }

    @Override
    public void clear() {
        final List<Entity> entities = GaeDatastoreTools.loadEntities(Kind.SNAPSHOT);

        for (final Entity entity : entities) {
            GaeDatastore.INSTANCE.getDatastoreService().delete(entity.getKey());
        }
    }

}
