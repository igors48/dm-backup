import org.junit.Before;
import org.junit.Test;
import service.*;

import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * Created by igor on 14.11.2016.
 */
public class BackupTest {

    private Loader loader;
    private Sender sender;
    private Recipients recipients;
    private Backup backup;
    private ServiceError serviceError;

    @Before
    public void setUp() throws Exception {
        this.loader = mock(Loader.class);
        this.sender = mock(Sender.class);
        this.serviceError = mock(ServiceError.class);

        this.recipients = new Recipients("a", Arrays.asList("b", "c"));

        this.backup = new Backup(this.loader, this.sender, this.recipients);
    }

    @Test
    public void whenContentReceivedThenItSendsToAllRecipients() throws Exception {
        when(loader.load()).thenReturn("content");

        backup.execute();

        verify(loader).load();
        verifyNoMoreInteractions(loader);

        verify(sender).sendContent("b", "content");
        verify(sender).sendContent("c", "content");
        verifyNoMoreInteractions(sender);
    }

    @Test
    public void whenContentNotReceivedThenItErrorSentToErrorRecipient() throws Exception {
        when(loader.load()).thenThrow(new ServiceException(this.serviceError));

        backup.execute();

        verify(loader).load();
        verifyNoMoreInteractions(loader);

        verify(sender).sendError("a", this.serviceError);
        verifyNoMoreInteractions(sender);
    }

}
