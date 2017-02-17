import org.junit.Test;
import service.Action;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * Created by igor on 14.11.2016.
 */
public class BackupSendTest extends BackupTestBase {

    @Test
    public void whenContentReceivedThenItSendsToChangesDetector() throws Exception {
        when(loader.load()).thenReturn(CONTENT);

        backup.execute();

        verify(loader).load();
        verifyNoMoreInteractions(loader);

        verify(changesDetector).getActionForContent(CONTENT.file);
        verifyNoMoreInteractions(changesDetector);
    }

    @Test
    public void whenContentChangesDetectedThenContentSendsToAllRecipients() throws Exception {
        when(loader.load()).thenReturn(CONTENT);
        when(changesDetector.getActionForContent(CONTENT.file)).thenReturn(Action.SEND);

        backup.execute();

        verify(sender).sendContent(A_B_COM, B_C_COM, CONTENT);
        verify(sender).sendContent(A_B_COM, C_D_COM, CONTENT);
        verifyNoMoreInteractions(sender);
    }

    @Test
    public void whenContentChangesNotDetectedThenContentNotSent() throws Exception {
        when(loader.load()).thenReturn(CONTENT);
        when(changesDetector.getActionForContent(CONTENT.file)).thenReturn(Action.NO_ACTION);

        backup.execute();

        verifyZeroInteractions(sender);
    }

    @Test
    public void whenContentCannotBeSentThenErrorSentToErrorRecipient() throws Exception {
        when(loader.load()).thenReturn(CONTENT);
        when(this.changesDetector.getActionForContent(CONTENT.file)).thenReturn(Action.SEND);
        doThrow(this.serviceException).when(sender).sendContent(A_B_COM, B_C_COM, CONTENT);

        backup.execute();

        verify(loader).load();
        verifyNoMoreInteractions(loader);

        verify(sender).sendContent(A_B_COM, B_C_COM, CONTENT);
        verify(sender).sendException(A_B_COM, this.serviceException);
        verify(sender).sendContent(A_B_COM, C_D_COM, CONTENT);
        verifyNoMoreInteractions(sender);
    }

    @Test
    public void whenContentNotReceivedThenErrorSentToErrorRecipient() throws Exception {
        when(loader.load()).thenThrow(this.serviceException);

        backup.execute();

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

            backup.execute();
        } catch (Exception exception) {
            fail();
        }
    }

}
