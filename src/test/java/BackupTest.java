import org.junit.Before;
import org.junit.Test;
import service.*;

import java.util.Arrays;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * Created by igor on 14.11.2016.
 */
public class BackupTest {

    private static final String CONTENT = "content";

    private Loader loader;
    private Sender sender;
    private Backup backup;
    private ServiceError serviceError;

    @Before
    public void setUp() throws Exception {
        this.loader = mock(Loader.class);
        this.sender = mock(Sender.class);
        this.serviceError = mock(ServiceError.class);

        final Recipients recipients = new Recipients("a", Arrays.asList("b", "c"));

        this.backup = new Backup(this.loader, this.sender, recipients);
    }

    @Test
    public void whenContentReceivedThenItSendsToAllRecipients() throws Exception {
        when(loader.load()).thenReturn(CONTENT);

        backup.execute();

        verify(loader).load();
        verifyNoMoreInteractions(loader);

        verify(sender).sendContent("b", CONTENT);
        verify(sender).sendContent("c", CONTENT);
        verifyNoMoreInteractions(sender);
    }

    @Test
    public void whenContentCannotBeSentThenErrorSentToErrorRecipient() throws Exception {
        when(loader.load()).thenReturn(CONTENT);

        doThrow(new ServiceException(this.serviceError)).when(sender).sendContent("b", CONTENT);

        backup.execute();

        verify(loader).load();
        verifyNoMoreInteractions(loader);

        verify(sender).sendContent("b", CONTENT);
        verify(sender).sendError("a", this.serviceError);
        verify(sender).sendContent("c", CONTENT);
        verifyNoMoreInteractions(sender);
    }

    @Test
    public void whenContentNotReceivedThenErrorSentToErrorRecipient() throws Exception {
        when(loader.load()).thenThrow(new ServiceException(this.serviceError));

        backup.execute();

        verify(loader).load();
        verifyNoMoreInteractions(loader);

        verify(sender).sendError("a", this.serviceError);
        verifyNoMoreInteractions(sender);
    }

    @Test
    public void whenErrorCanNotBeSentThenNoExceptionThrown() throws Exception {

        try {
            when(loader.load()).thenThrow(new ServiceException(this.serviceError));
            doThrow(new ServiceException(this.serviceError)).when(sender).sendError("a", this.serviceError);

            backup.execute();
        } catch (Exception exception) {
            fail();
        }
    }

}
