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

    private ContentRepository contentRepository;
    private TimestampRepository timestampRepository;
    private TimeService timeService;
    private long waitInMillis;
    private Transactions transactions;
    private TransactionStub transactionStub;

    private ChangesDetector changesDetector;

    @Before
    public void setUp() throws Exception {
        this.contentRepository = mock(ContentRepository.class);
        this.timestampRepository = mock(TimestampRepository.class);
        this.timeService = mock(TimeService.class);
        this.waitInMillis = 48;
        this.transactions = mock(Transactions.class);
        this.transactionStub = new TransactionStub();

        when(this.transactions.beginOne()).thenReturn(this.transactionStub);

        this.changesDetector = new ChangesDetector(this.contentRepository, this.timestampRepository, this.timeService, this.waitInMillis, this.transactions);
    }

    @Test
    public void transactionCreatedAndCommitted() throws Exception {
        this.changesDetector.contentMustBeSent("content");

        assertEquals(TransactionState.COMMITTED, this.transactionStub.getState());
    }

    @Test
    public void transactionCreatedAndRolledBackInCaseOfException() throws Exception {

        try {
            when(this.contentRepository.loadLatestSnapshot()).thenThrow(new RuntimeException());

            this.changesDetector.contentMustBeSent("content");

            fail();
        } catch (Exception exception) {
            assertEquals(TransactionState.ROLLED_BACK, this.transactionStub.getState());
        }
    }

}
