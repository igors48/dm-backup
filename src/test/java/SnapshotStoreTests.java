import org.junit.Test;
import service.SnapshotRepository;
import service.SnapshotStore;
import service.TimeService;

/**
 * Created by igor on 22.02.2017.
 */
public class SnapshotStoreTests {

    private SnapshotStore snapshotStore;
    private SnapshotRepository snapshotRepository;
    private TimeService timeService;

    @Test
    public void smoke() throws Exception {
        this.snapshotRepository = new SnapshotRepositoryStub();
        this.timeService = new TimeServiceStub();

        this.snapshotStore = new SnapshotStore(2, this.snapshotRepository, this.timeService);
    }

}
