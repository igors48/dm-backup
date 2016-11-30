import org.junit.Before;
import org.junit.Test;
import service.Backup;
import service.ChangesDetector;
import service.Loader;
import service.Sender;
import service.configuration.Recipients;
import service.error.ServiceException;

import java.util.Arrays;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * Created by igor on 14.11.2016.
 */
public class BackupTest {

    private static final String CONTENT = "content";
    private static final String A_B_COM = "a@b.com";
    private static final String B_C_COM = "b@c.com";
    private static final String C_D_COM = "c@d.com";

    private Loader loader;
    private Sender sender;
    private ChangesDetector changesDetector;
    private Backup backup;
    private ServiceException serviceException;

    @Before
    public void setUp() throws Exception {
        this.loader = mock(Loader.class);
        this.sender = mock(Sender.class);
        this.changesDetector = mock(ChangesDetector.class);

        this.serviceException = mock(ServiceException.class);

        final Recipients recipients = new Recipients(A_B_COM, Arrays.asList(B_C_COM, C_D_COM));

        when(this.changesDetector.contentMustBeSent(CONTENT)).thenReturn(true);

        this.backup = new Backup(this.loader, this.sender, this.changesDetector, recipients);
    }

    @Test
    public void whenContentReceivedThenItSendsToChangesDetector() throws Exception {
        when(loader.load()).thenReturn(CONTENT);

        backup.execute();

        verify(loader).load();
        verifyNoMoreInteractions(loader);

        verify(changesDetector).contentMustBeSent(CONTENT);
        verifyNoMoreInteractions(changesDetector);
    }

    @Test
    public void whenContentChangesDetectedThenContentSendsToAllRecipients() throws Exception {
        when(loader.load()).thenReturn(CONTENT);
        when(changesDetector.contentMustBeSent(CONTENT)).thenReturn(true);

        backup.execute();

        verify(sender).sendContent(A_B_COM, B_C_COM, CONTENT);
        verify(sender).sendContent(A_B_COM, C_D_COM, CONTENT);
        verifyNoMoreInteractions(sender);
    }

    @Test
    public void whenContentChangesNotDetectedThenContentNotSent() throws Exception {
        when(loader.load()).thenReturn(CONTENT);
        when(changesDetector.contentMustBeSent(CONTENT)).thenReturn(false);

        backup.execute();

        verifyZeroInteractions(sender);
    }

    @Test
    public void whenContentCannotBeSentThenErrorSentToErrorRecipient() throws Exception {
        when(loader.load()).thenReturn(CONTENT);

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
