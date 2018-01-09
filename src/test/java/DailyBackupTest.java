import org.junit.Test;
import util.account.Account;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * Created by igor on 26.02.2017.
 */
public class DailyBackupTest extends BackupTestBase {

    @Test
    public void whenContentReceivedThenItSendsToAllRecipients() throws Exception {
        backup.dailyBackup(CONTENT);

        verify(sender).sendDailyBackup(A_B_COM, B_C_COM, CONTENT, LATEST_ACCOUNTS, new Date(LATEST_SNAPSHOT.timestamp), new Date(CURRENT_SNAPSHOT.timestamp));
        verify(sender).sendDailyBackup(A_B_COM, C_D_COM, CONTENT, LATEST_ACCOUNTS, new Date(LATEST_SNAPSHOT.timestamp), new Date(CURRENT_SNAPSHOT.timestamp));
        verifyNoMoreInteractions(sender);
    }

    @Test
    public void whenContentReceivedThenItStores() throws Exception {
        backup.dailyBackup(CONTENT);

        verify(dailySnapshotStore).store(CONTENT);
    }

    @Test
    public void whenContentCannotBeSentThenErrorSentToErrorRecipient() throws Exception {
        doThrow(this.serviceException).when(sender).sendDailyBackup(A_B_COM, B_C_COM, CONTENT, LATEST_ACCOUNTS, new Date(LATEST_SNAPSHOT.timestamp), new Date(CURRENT_SNAPSHOT.timestamp));

        backup.dailyBackup(CONTENT);

        verify(sender).sendDailyBackup(A_B_COM, B_C_COM, CONTENT, LATEST_ACCOUNTS, new Date(LATEST_SNAPSHOT.timestamp), new Date(CURRENT_SNAPSHOT.timestamp));
        verify(sender).sendException(A_B_COM, this.serviceException);
        verify(sender).sendDailyBackup(A_B_COM, C_D_COM, CONTENT, LATEST_ACCOUNTS, new Date(LATEST_SNAPSHOT.timestamp), new Date(CURRENT_SNAPSHOT.timestamp));
        verifyNoMoreInteractions(sender);
    }

    @Test
    public void whenErrorCanNotBeSentThenNoExceptionThrown() throws Exception {

        try {
            doThrow(this.serviceException).when(sender).sendException(A_B_COM, this.serviceException);

            backup.dailyBackup(CONTENT);
        } catch (Exception exception) {
            fail();
        }
    }

    @Test
    public void whenNoLatestDailySnapshotThenDefaultValuesUsed() throws Exception {
        when(this.dailySnapshotStore.loadLatestSnapshot()).thenReturn(null);

        backup.dailyBackup(CONTENT);

        verify(sender).sendDailyBackup(A_B_COM, B_C_COM, CONTENT, new ArrayList<Account>(), new Date(timeServiceStub.currentTimestamp()), new Date(CURRENT_SNAPSHOT.timestamp));
        verify(sender).sendDailyBackup(A_B_COM, C_D_COM, CONTENT, new ArrayList<Account>(), new Date(timeServiceStub.currentTimestamp()), new Date(CURRENT_SNAPSHOT.timestamp));
    }

}
