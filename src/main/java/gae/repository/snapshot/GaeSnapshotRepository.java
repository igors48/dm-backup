package gae.repository.snapshot;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.repackaged.com.google.datastore.v1.Datastore;
import gae.repository.Converter;
import gae.repository.GaeDatastore;
import gae.repository.GaeDatastoreTools;
import gae.repository.Kind;
import service.Snapshot;
import service.SnapshotRepository;

import java.util.ArrayList;
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
        final List<Entity> entities = GaeDatastoreTools.loadEntities(Kind.SNAPSHOT);

        Snapshot latest = null;

        for (final Entity entity : entities) {
            final Snapshot snapshot = this.converter.convert(entity);

            if ((latest == null) || (snapshot.timestamp > latest.timestamp)) {
                latest = snapshot;
            }
        }

        return latest;
    }

    @Override
    public List<Snapshot> loadAll() {
        final List<Snapshot> result = new ArrayList<>();

        final List<Entity> entities = GaeDatastoreTools.loadEntities(Kind.SNAPSHOT);

        for (final Entity entity : entities) {
            final Snapshot snapshot = this.converter.convert(entity);
            result.add(snapshot);
        }

        return result;
    }

    @Override
    public void storeAll(final List<Snapshot> snapshots) {
        guard(notNull(snapshots));
        this.clear();

        for (final Snapshot snapshot: snapshots) {
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
