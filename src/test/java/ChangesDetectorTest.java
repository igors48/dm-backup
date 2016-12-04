import org.junit.Before;
import org.junit.Test;
import service.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by igor on 26.11.2016.
 */
public class ChangesDetectorTest {

    private SnapshotRepository snapshotRepository;
    private TimestampRepository timestampRepository;
    private TimeService timeService;
    private long waitInMillis;
    private Transactions transactions;
    private TransactionStub transactionStub;

    private ChangesDetector changesDetector;

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
        when(this.timeService.currentTimestamp()).thenReturn(42L);

        this.changesDetector = new ChangesDetector(this.snapshotRepository, this.timestampRepository, this.timeService, this.waitInMillis, this.transactions);
    }

    @Test
    public void transactionCreatedAndCommitted() throws Exception {
        this.changesDetector.getActionForContent("content");

        assertEquals(TransactionState.COMMITTED, this.transactionStub.getState());
    }

    @Test
    public void transactionCreatedAndRolledBackInCaseOfException() throws Exception {

        try {
            when(this.snapshotRepository.loadLatestSnapshot()).thenThrow(new RuntimeException());

            this.changesDetector.getActionForContent("content");

            fail();
        } catch (Exception exception) {
            assertEquals(TransactionState.ROLLED_BACK, this.transactionStub.getState());
        }
    }

}
