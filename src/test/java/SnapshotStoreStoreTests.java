import org.junit.Before;
import org.junit.Test;
import service.*;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by igor on 22.02.2017.
 */
public class SnapshotStoreStoreTests {

    private static final int MAXIMUM_CAPACITY = 2;

    private SnapshotStore snapshotStore;
    private SnapshotRepository snapshotRepository;
    private TimeService timeService;

    @Before
    public void setUp() throws Exception {
        this.snapshotRepository = new SnapshotRepositoryStub();
        this.timeService = new TimeServiceStub();

        this.snapshotStore = new SnapshotStore(MAXIMUM_CAPACITY, this.snapshotRepository, this.timeService);
    }

    @Test
    public void whenContentStoredThenItIsStoredInRepository() {
        List<Snapshot> snapshots = this.snapshotRepository.loadAll();
        assertEquals(0, snapshots.size());

        final Content content = TestData.createContent();
        this.snapshotStore.store(content);

        snapshots = this.snapshotRepository.loadAll();
        assertEquals(1, snapshots.size());
    }

    @Test
    public void totalCountOfStoredSnapshotsNeverGreaterThenConfigured() {
        final Content first = TestData.createContent("first");
        this.snapshotStore.store(first);

        final Content second = TestData.createContent("second");
        this.snapshotStore.store(second);

        final Content third = TestData.createContent("third");
        this.snapshotStore.store(third);

        final List<Snapshot> snapshots = this.snapshotRepository.loadAll();
        assertEquals(MAXIMUM_CAPACITY, snapshots.size());
    }

    @Test
    public void whenNewItemOverCapacityThenOldestRemoved() {
        final Content first = TestData.createContent("first");
        this.snapshotStore.store(first);

        final Content second = TestData.createContent("second");
        this.snapshotStore.store(second);

        final Content third = TestData.createContent("third");
        this.snapshotStore.store(third);

        final List<Snapshot> snapshots = this.snapshotRepository.loadAll();

        assertEquals(second, snapshots.get(0).content);
        assertEquals(third, snapshots.get(1).content);
    }

}
