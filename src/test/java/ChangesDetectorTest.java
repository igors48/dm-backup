import org.junit.Before;
import org.junit.Test;
import service.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
        this.changesDetector.contentMustBeSent("content");

        assertEquals(TransactionState.COMMITTED, this.transactionStub.getState());
    }

    @Test
    public void transactionCreatedAndRolledBackInCaseOfException() throws Exception {

        try {
            when(this.snapshotRepository.loadLatestSnapshot()).thenThrow(new RuntimeException());

            this.changesDetector.contentMustBeSent("content");

            fail();
        } catch (Exception exception) {
            assertEquals(TransactionState.ROLLED_BACK, this.transactionStub.getState());
        }
    }

    @Test
    public void whenContentChangedThenContentMustNotSaved() throws Exception {
        final boolean contentMustBeSent = this.changesDetector.contentMustBeSent("b");

        assertFalse(contentMustBeSent);
    }

    @Test
    public void whenContentChangedThenTimestapSetToCurrentTime() throws Exception {
        this.changesDetector.contentMustBeSent("b");

        verify(this.timestampRepository).store(42L);
    }

    @Test
    public void whenContentNotChangedAndWaitPeriodIsNotExpiredThenContentIsNotSaved() throws Exception {
        final boolean contentMustBeSent = this.changesDetector.contentMustBeSent("a");

        assertFalse(contentMustBeSent);
    }

    @Test
    public void whenContentNotChangedAndNoStoredTimestampThenContentIsNotSaved() throws Exception {
        when(this.timestampRepository.load()).thenReturn(null);

        final boolean contentMustBeSent = this.changesDetector.contentMustBeSent("a");

        assertFalse(contentMustBeSent);
    }

    @Test
    public void whenContentNotChangedAndWaitPeriodIsExpiredThenContentIsSaved() throws Exception {
        this.changesDetector.contentMustBeSent("b");

        when(this.timeService.currentTimestamp()).thenReturn(42 + this.waitInMillis + 1);

        when(this.transactions.beginOne()).thenReturn(new TransactionStub());

        final boolean contentMustBeSent = this.changesDetector.contentMustBeSent("a");

        assertTrue(contentMustBeSent);
    }

    @Test
    public void whenContentNotChangedAndWaitPeriodIsExpiredThenTimestampRemoved() throws Exception {
        this.changesDetector.contentMustBeSent("b");

        when(this.timeService.currentTimestamp()).thenReturn(42 + this.waitInMillis + 1);

        when(this.transactions.beginOne()).thenReturn(new TransactionStub());

        this.changesDetector.contentMustBeSent("a");

        verify(this.timestampRepository).store(42L);
        verify(this.timestampRepository).clear();
    }

}
