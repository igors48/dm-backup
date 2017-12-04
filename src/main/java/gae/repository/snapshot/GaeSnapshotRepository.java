package gae.repository.snapshot;

import com.google.appengine.api.datastore.DatastoreService;
import gae.repository.Converter;
import gae.repository.Repository;
import service.Snapshot;
import service.SnapshotRepository;

import java.util.Collections;
import java.util.List;

/**
 * Created by igor on 29.11.2016.
 */
public class GaeSnapshotRepository extends Repository<Snapshot> implements SnapshotRepository {

    public GaeSnapshotRepository(final DatastoreService service, final Converter<Snapshot> converter) {
        super(service, converter);
    }

    @Override
    public List<Snapshot> loadAll() {
        final List<Snapshot> snapshots = super.loadAll();
        Collections.sort(snapshots, Snapshot.TIMESTAMP_COMPARATOR);

        return snapshots;
    }

    @Override
    public Snapshot loadPreLatestSnapshot() {
        final List<Snapshot> snapshots = this.loadAll();

        final int count = snapshots.size();

        return count < 3 ? null : snapshots.get(count - 2);
    }

    @Override
    public Snapshot loadLatestSnapshot() {
        final List<Snapshot> snapshots = this.loadAll();

        final int count = snapshots.size();

        return count == 0 ? null : snapshots.get(count - 1);
    }

}
