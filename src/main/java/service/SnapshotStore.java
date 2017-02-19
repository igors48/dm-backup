package service;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 17.02.2017.
 */
public class SnapshotStore {

    private final SnapshotRepository snapshotRepository;
    private final TimeService timeService;

    public SnapshotStore(final SnapshotRepository snapshotRepository, final TimeService timeService) {
        guard(notNull(this.snapshotRepository = snapshotRepository));
        guard(notNull(this.timeService = timeService));
    }

    public void updateLast(final Content content) {
        guard(notNull(content));
    }

    public void store(final Content content) {
        guard(notNull(content));

    }

}
