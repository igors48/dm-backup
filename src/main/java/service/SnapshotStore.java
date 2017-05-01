package service;

import java.util.List;
import java.util.UUID;

import static util.Assert.guard;
import static util.Parameter.isPositive;
import static util.Parameter.notNull;

/**
 * Created by igor on 17.02.2017.
 */
public class SnapshotStore {

    private final int capacity;
    private final Type type;
    private final SnapshotRepository snapshotRepository;
    private final TimeService timeService;

    public SnapshotStore(final int capacity, final Type type, final SnapshotRepository snapshotRepository, final TimeService timeService) {
        guard(isPositive(this.capacity = capacity));
        guard(notNull(this.type = type));
        guard(notNull(this.snapshotRepository = snapshotRepository));
        guard(notNull(this.timeService = timeService));
    }

    public void updateLast(final Content content) {
        guard(notNull(content));

        final List<Snapshot> snapshots = this.snapshotRepository.loadAll();
        final int size = snapshots.size();

        if (size > 0) {
            snapshots.remove(size - 1);
        }

        this.append(content, snapshots);
    }

    public void store(final Content content) {
        guard(notNull(content));

        final List<Snapshot> snapshots = this.snapshotRepository.loadAll();

        this.append(content, snapshots);
    }

    public Snapshot loadLatestSnapshot() {
        return this.snapshotRepository.loadLatestSnapshot();
    }

    private void append(final Content content, final List<Snapshot> source) {
        final Snapshot snapshot = this.createFor(content);
        source.add(snapshot);

        final List<Snapshot> result = this.checkCapacity(source);

        this.snapshotRepository.storeAll(result);
    }

    private List<Snapshot> checkCapacity(final List<Snapshot> list) {
        final int size = list.size();

        return size > this.capacity ? list.subList(size - capacity, size) : list;
    }

    private Snapshot createFor(final Content content) {
        final UUID uuid = UUID.randomUUID();
        final long timestamp = this.timeService.currentTimestamp();

        return new Snapshot(uuid, type, timestamp, content);
    }

}
