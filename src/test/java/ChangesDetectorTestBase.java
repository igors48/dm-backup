import org.junit.Before;
import service.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by igor on 26.11.2016.
 */
public class ChangesDetectorTestBase {

    protected static final long CURRENT_TIMESTAMP = 42L;

    protected SnapshotRepository snapshotRepository;
    protected TimestampRepository timestampRepository;
    protected TimeService timeService;
    protected long waitInMillis;
    protected Transactions transactions;
    protected TransactionStub transactionStub;

    protected ChangesDetector changesDetector;

    @Before
    public void setUp() throws Exception {
        this.snapshotRepository = mock(SnapshotRepository.class);
        this.timestampRepository = mock(TimestampRepository.class);
        this.timeService = mock(TimeService.class);
        this.waitInMillis = 48;
        this.transactions = mock(Transactions.class);
        this.transactionStub = new TransactionStub();

        when(this.transactions.beginOne()).thenReturn(this.transactionStub);
        when(this.snapshotRepository.loadLatestSnapshot()).thenReturn("a");
        when(this.timeService.currentTimestamp()).thenReturn(CURRENT_TIMESTAMP);

        this.changesDetector = new ChangesDetector(this.snapshotRepository, this.timestampRepository, this.timeService, this.waitInMillis, this.transactions);
    }

}
