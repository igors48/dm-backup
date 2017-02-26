import org.junit.Test;
import service.Action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * Created by igor on 17.02.2017.
 */
public class BackupSnapshotStoreTest extends BackupTestBase {

    @Test
    public void whenActionIsSaveThenContentStored() {
        when(this.changesDetector.getActionForContent(CONTENT.file)).thenReturn(Action.SAVE);

        this.backup.checkChanges();

        verify(this.snapshotStore).store(CONTENT);
        verifyNoMoreInteractions(this.snapshotStore);
        assertEquals(TransactionState.COMMITTED, this.transactionStub.getState());
    }

    @Test
    public void whenActionIsUpdateThenContentUpdated() {
        when(this.changesDetector.getActionForContent(CONTENT.file)).thenReturn(Action.UPDATE_LAST);

        this.backup.checkChanges();

        verify(this.snapshotStore).updateLast(CONTENT);
        verifyNoMoreInteractions(this.snapshotStore);
        assertEquals(TransactionState.COMMITTED, this.transactionStub.getState());
    }

    @Test
    public void whenActionIsSendThenNoContentActions() {
        when(this.changesDetector.getActionForContent(CONTENT.file)).thenReturn(Action.SEND);

        this.backup.checkChanges();

        verifyZeroInteractions(this.snapshotStore);
    }

    @Test
    public void whenNoActionThenNoContentActions() {
        when(this.changesDetector.getActionForContent(CONTENT.file)).thenReturn(Action.NO_ACTION);

        this.backup.checkChanges();

        verifyZeroInteractions(this.snapshotStore);
    }

    @Test
    public void whenActionIsSaveAndContentStoreFailedThenTransactionRolledBack() {

        try {
            when(this.changesDetector.getActionForContent(CONTENT.file)).thenReturn(Action.SAVE);
            doThrow(new RuntimeException()).when(this.snapshotStore).store(CONTENT);

            this.backup.checkChanges();

            fail();
        } catch (Exception e) {
            assertEquals(TransactionState.ROLLED_BACK, this.transactionStub.getState());
        }
    }

    @Test
    public void whenActionIsUpdateAndContentStoreFailedThenTransactionRolledBack() {

        try {
            when(this.changesDetector.getActionForContent(CONTENT.file)).thenReturn(Action.UPDATE_LAST);
            doThrow(new RuntimeException()).when(this.snapshotStore).updateLast(CONTENT);

            this.backup.checkChanges();

            fail();
        } catch (Exception e) {
            assertEquals(TransactionState.ROLLED_BACK, this.transactionStub.getState());
        }
    }

}
