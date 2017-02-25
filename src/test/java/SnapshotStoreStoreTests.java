import org.junit.Test;
import service.Content;
import service.Snapshot;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by igor on 22.02.2017.
 */
public class SnapshotStoreStoreTests extends SnapshotStoreTestBase {

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
