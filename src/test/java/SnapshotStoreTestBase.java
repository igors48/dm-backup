import org.junit.Before;
import service.SnapshotRepository;
import service.SnapshotStore;
import service.TimeService;
import service.Type;

/**
 * Created by igor on 25.02.2017.
 */
public class SnapshotStoreTestBase {

    protected static final int MAXIMUM_CAPACITY = 2;

    protected SnapshotStore snapshotStore;
    protected SnapshotRepository snapshotRepository;
    private TimeService timeService;

    @Before
    public void setUp() throws Exception {
        this.snapshotRepository = new SnapshotRepositoryStub();
        this.timeService = new TimeServiceStub();

        this.snapshotStore = new SnapshotStore(MAXIMUM_CAPACITY, Type.CHANGE, this.snapshotRepository, this.timeService);
    }

}
