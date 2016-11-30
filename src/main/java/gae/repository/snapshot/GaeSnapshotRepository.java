package gae.repository.snapshot;

import com.google.appengine.api.datastore.Entity;
import gae.repository.AbstractConverter;
import gae.repository.GaeDatastoreTools;
import gae.repository.Kind;
import service.Snapshot;
import service.SnapshotRepository;

import java.util.List;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 29.11.2016.
 */
public class GaeSnapshotRepository implements SnapshotRepository {

    private final AbstractConverter<Snapshot> converter;

    public GaeSnapshotRepository(final AbstractConverter<Snapshot> converter) {
        guard(notNull(this.converter = converter));
    }

    @Override
    public String loadLatestSnapshot() {
        final List<Entity> entities = GaeDatastoreTools.loadEntities(Kind.SNAPSHOT);

        Snapshot latest = null;

        for (final Entity entity : entities) {
            final Snapshot snapshot = this.converter.convert(entity);

            if ((latest == null) || (snapshot.timestamp > latest.timestamp)) {
                latest = snapshot;
            }
        }

        return latest == null ? "" : latest.content;
    }

    @Override
    public List<Snapshot> loadAll() {
        return null;
    }

    @Override
    public void storeAll(final List<Snapshot> snapshots) {

    }

    @Override
    public void clear() {

    }

}
