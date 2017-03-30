import org.junit.Test;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * Created by igor on 26.02.2017.
 */
public class DailyBackupTest extends BackupTestBase {

    @Test
    public void whenContentReceivedThenItSendsToAllRecipients() throws Exception {
        when(loader.load()).thenReturn(CONTENT);

        backup.dailyBackup();

        verify(loader).load();
        verifyNoMoreInteractions(loader);

        verify(sender).sendDailyBackup(A_B_COM, B_C_COM, CONTENT);
        verify(sender).sendDailyBackup(A_B_COM, C_D_COM, CONTENT);
        verifyNoMoreInteractions(sender);
    }

    @Test
    public void whenContentReceivedThenItStores() throws Exception {
        when(loader.load()).thenReturn(CONTENT);

        backup.dailyBackup();

        verify(loader).load();
        verifyNoMoreInteractions(loader);

        verify(dailySnapshotStore).store(CONTENT);
        verifyNoMoreInteractions(dailySnapshotStore);
    }

    @Test
    public void whenContentCannotBeSentThenErrorSentToErrorRecipient() throws Exception {
        when(loader.load()).thenReturn(CONTENT);

        doThrow(this.serviceException).when(sender).sendDailyBackup(A_B_COM, B_C_COM, CONTENT);

        backup.dailyBackup();

        verify(loader).load();
        verifyNoMoreInteractions(loader);

        verify(sender).sendDailyBackup(A_B_COM, B_C_COM, CONTENT);
        verify(sender).sendException(A_B_COM, this.serviceException);
        verify(sender).sendDailyBackup(A_B_COM, C_D_COM, CONTENT);
        verifyNoMoreInteractions(sender);
    }

    @Test
    public void whenContentNotReceivedThenErrorSentToErrorRecipient() throws Exception {
        when(loader.load()).thenThrow(this.serviceException);

        backup.dailyBackup();

        verify(loader).load();
        verifyNoMoreInteractions(loader);

        verify(sender).sendException(A_B_COM, this.serviceException);
        verifyNoMoreInteractions(sender);
    }

    @Test
    public void whenErrorCanNotBeSentThenNoExceptionThrown() throws Exception {

        try {
            when(loader.load()).thenThrow(this.serviceException);
            doThrow(this.serviceException).when(sender).sendException(A_B_COM, this.serviceException);

            backup.dailyBackup();
        } catch (Exception exception) {
            fail();
        }
    }

}
