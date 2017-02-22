import org.junit.Before;
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

    @Before
    public void setUp() throws Exception {
        this.snapshotRepository = new SnapshotRepositoryStub();
        this.timeService = new TimeServiceStub();

        this.snapshotStore = new SnapshotStore(2, this.snapshotRepository, this.timeService);
    }

    @Test
    public void when() throws Exception {
        // get content from TestData
    }

}
