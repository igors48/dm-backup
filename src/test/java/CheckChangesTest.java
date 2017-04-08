import org.junit.Test;
import service.Action;
import util.account.Account;

import java.util.ArrayList;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * Created by igor on 14.11.2016.
 */
public class CheckChangesTest extends BackupTestBase {

    @Test
    public void whenContentReceivedThenItSendsToChangesDetector() throws Exception {
        this.backup.checkChanges();

        verify(this.loader).load();
        verifyNoMoreInteractions(this.loader);

        verify(changesDetector).getActionForContent(CONTENT.file);
        verifyNoMoreInteractions(changesDetector);
    }

    @Test
    public void whenContentChangesDetectedThenContentSendsToAllRecipients() throws Exception {
        when(this.changesDetector.getActionForContent(CONTENT.file)).thenReturn(Action.send(CONTENT.accounts));

        this.backup.checkChanges();

        verify(this.sender).sendChangedContent(A_B_COM, B_C_COM, CONTENT, new ArrayList<Account>());
        verify(this.sender).sendChangedContent(A_B_COM, C_D_COM, CONTENT, new ArrayList<Account>());
        verifyNoMoreInteractions(this.sender);
    }

    @Test
    public void whenContentChangesNotDetectedThenContentNotSent() throws Exception {
        when(this.changesDetector.getActionForContent(CONTENT.file)).thenReturn(Action.NO_ACTION);

        this.backup.checkChanges();

        verifyZeroInteractions(sender);
    }

    @Test
    public void whenContentCannotBeSentThenErrorSentToErrorRecipient() throws Exception {
        when(this.changesDetector.getActionForContent(CONTENT.file)).thenReturn(Action.send(CONTENT.accounts));
        doThrow(this.serviceException).when(sender).sendChangedContent(A_B_COM, B_C_COM, CONTENT, new ArrayList<Account>());

        this.backup.checkChanges();

        verify(this.loader).load();
        verifyNoMoreInteractions(this.loader);

        verify(this.sender).sendChangedContent(A_B_COM, B_C_COM, CONTENT, new ArrayList<Account>());
        verify(this.sender).sendException(A_B_COM, this.serviceException);
        verify(this.sender).sendChangedContent(A_B_COM, C_D_COM, CONTENT, new ArrayList<Account>());
        verifyNoMoreInteractions(this.sender);
    }

    @Test
    public void whenContentNotReceivedThenErrorSentToErrorRecipient() throws Exception {
        when(this.loader.load()).thenThrow(this.serviceException);

        this.backup.checkChanges();

        verify(this.loader).load();
        verifyNoMoreInteractions(this.loader);

        verify(this.sender).sendException(A_B_COM, this.serviceException);
        verifyNoMoreInteractions(this.sender);
    }

    @Test
    public void whenErrorCanNotBeSentThenNoExceptionThrown() throws Exception {

        try {
            when(this.loader.load()).thenThrow(this.serviceException);
            doThrow(this.serviceException).when(this.sender).sendException(A_B_COM, this.serviceException);

            this.backup.checkChanges();
        } catch (Exception exception) {
            fail();
        }
    }

}
