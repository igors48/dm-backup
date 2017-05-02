import org.junit.Test;
import service.Action;

import java.util.Date;

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

        verify(this.changesSnapshotStore).store(CONTENT);
        verifyNoMoreInteractions(this.changesSnapshotStore);
        assertEquals(TransactionState.COMMITTED, this.transactionStub.getState());
    }

    @Test
    public void whenActionIsUpdateThenContentUpdated() {
        when(this.changesDetector.getActionForContent(CONTENT.file)).thenReturn(Action.UPDATE_LAST);

        this.backup.checkChanges();

        verify(this.changesSnapshotStore).updateLast(CONTENT);
        verifyNoMoreInteractions(this.changesSnapshotStore);
        assertEquals(TransactionState.COMMITTED, this.transactionStub.getState());
    }

    @Test
    public void whenActionIsSendThenNoContentActions() {
        when(this.changesDetector.getActionForContent(CONTENT.file)).thenReturn(Action.send(CONTENT.accounts, new Date(), new Date()));

        this.backup.checkChanges();

        verifyZeroInteractions(this.changesSnapshotStore);
    }

    @Test
    public void whenNoActionThenNoContentActions() {
        when(this.changesDetector.getActionForContent(CONTENT.file)).thenReturn(Action.NO_ACTION);

        this.backup.checkChanges();

        verifyZeroInteractions(this.changesSnapshotStore);
    }

    @Test
    public void whenActionIsSaveAndContentStoreFailedThenTransactionRolledBack() {

        try {
            when(this.changesDetector.getActionForContent(CONTENT.file)).thenReturn(Action.SAVE);
            doThrow(new RuntimeException()).when(this.changesSnapshotStore).store(CONTENT);

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
            doThrow(new RuntimeException()).when(this.changesSnapshotStore).updateLast(CONTENT);

            this.backup.checkChanges();

            fail();
        } catch (Exception e) {
            assertEquals(TransactionState.ROLLED_BACK, this.transactionStub.getState());
        }
    }

}
