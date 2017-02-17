import org.junit.Test;
import service.Action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * Created by igor on 17.02.2017.
 */
public class BackupContentStoreTest extends BackupTestBase {

    @Test
    public void whenActionIsSaveThenContentStored() {
        when(this.changesDetector.getActionForContent(CONTENT.file)).thenReturn(Action.SAVE);

        this.backup.execute();

        verify(this.contentStore).store(CONTENT);
        verifyNoMoreInteractions(this.contentStore);
        assertEquals(TransactionState.COMMITTED, this.transactionStub.getState());
    }

    @Test
    public void whenActionIsUpdateThenContentUpdated() {
        when(this.changesDetector.getActionForContent(CONTENT.file)).thenReturn(Action.UPDATE_LAST);

        this.backup.execute();

        verify(this.contentStore).updateLast(CONTENT);
        verifyNoMoreInteractions(this.contentStore);
        assertEquals(TransactionState.COMMITTED, this.transactionStub.getState());
    }

    @Test
    public void whenActionIsSendThenNoContentActions() {
        when(this.changesDetector.getActionForContent(CONTENT.file)).thenReturn(Action.SEND);

        this.backup.execute();

        verifyZeroInteractions(this.contentStore);
    }

    @Test
    public void whenNoActionThenNoContentActions() {
        when(this.changesDetector.getActionForContent(CONTENT.file)).thenReturn(Action.NO_ACTION);

        this.backup.execute();

        verifyZeroInteractions(this.contentStore);
    }

    @Test
    public void whenActionIsSaveAndContentStoreFailedThenTransactionRolledBack() {

        try {
            when(this.changesDetector.getActionForContent(CONTENT.file)).thenReturn(Action.SAVE);
            doThrow(new RuntimeException()).when(this.contentStore).store(CONTENT);

            this.backup.execute();

            fail();
        } catch (Exception e) {
            assertEquals(TransactionState.ROLLED_BACK, this.transactionStub.getState());
        }
    }

    @Test
    public void whenActionIsUpdateAndContentStoreFailedThenTransactionRolledBack() {

        try {
            when(this.changesDetector.getActionForContent(CONTENT.file)).thenReturn(Action.UPDATE_LAST);
            doThrow(new RuntimeException()).when(this.contentStore).updateLast(CONTENT);

            this.backup.execute();

            fail();
        } catch (Exception e) {
            assertEquals(TransactionState.ROLLED_BACK, this.transactionStub.getState());
        }
    }

}
