import org.junit.Test;
import service.Content;
import service.Snapshot;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by igor on 25.02.2017.
 */
public class SnapshotStoreUpdateLastTests extends SnapshotStoreTestBase {

    @Test
    public void whenStoreIsEmptyThenSnapshotJustAdded() {
        final Content content = TestData.createContent("a");

        this.snapshotStore.updateLast(content);

        final List<Snapshot> snapshots = this.snapshotRepository.loadAll();

        assertEquals(1, snapshots.size());
        assertEquals(content, snapshots.get(0).content);
    }

    @Test
    public void whenStoreContainsOneSnapshotThenItIsUpdated() {
        final Content first = TestData.createContent("first");
        this.snapshotStore.updateLast(first);

        final Content second = TestData.createContent("second");
        this.snapshotStore.updateLast(second);

        final List<Snapshot> snapshots = this.snapshotRepository.loadAll();

        assertEquals(1, snapshots.size());
        assertEquals(second, snapshots.get(0).content);
    }

    @Test
    public void whenStoreContainsTwoSnapshotsThenLastIsUpdated() {
        final Content first = TestData.createContent("first");
        this.snapshotStore.store(first);

        final Content second = TestData.createContent("second");
        this.snapshotStore.store(second);

        final Content third = TestData.createContent("third");
        this.snapshotStore.updateLast(third);

        final List<Snapshot> snapshots = this.snapshotRepository.loadAll();

        assertEquals(MAXIMUM_CAPACITY, snapshots.size());
        assertEquals(first, snapshots.get(0).content);
        assertEquals(third, snapshots.get(1).content);
    }

}
