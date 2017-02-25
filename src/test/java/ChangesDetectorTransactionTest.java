import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * Created by igor on 04.12.2016.
 */
public class ChangesDetectorTransactionTest extends ChangesDetectorTestBase {

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
