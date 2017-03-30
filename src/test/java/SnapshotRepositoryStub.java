import service.Snapshot;
import service.SnapshotRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by igor on 22.02.2017.
 */
public class SnapshotRepositoryStub implements SnapshotRepository {

    private List<Snapshot> snapshots = new ArrayList<>();

    @Override
    public Snapshot loadLatestSnapshot() {
        Collections.sort(this.snapshots, Snapshot.TIMESTAMP_COMPARATOR);

        final int size = this.snapshots.size();

        return size == 0 ? null : this.snapshots.get(size - 1);
    }

    @Override
    public List<Snapshot> loadAll() {
        Collections.sort(this.snapshots, Snapshot.TIMESTAMP_COMPARATOR);

        return new ArrayList<>(this.snapshots);
    }

    @Override
    public void storeAll(final List<Snapshot> snapshots) {
        this.snapshots.clear();
        this.snapshots.addAll(snapshots);
    }

    @Override
    public void clear() {
        this.snapshots.clear();
    }

}
