import org.junit.Test;
import service.Action;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by igor on 04.12.2016.
 */
public class ChangesDetectorContentNotChangedTest extends ChangesDetectorTestBase {

    @Test
    public void whenNoStoredTimestampThenNoAction() throws Exception {
        when(this.timestampRepository.load()).thenReturn(null);

        final Action action = this.changesDetector.getActionForContent("a");

        assertEquals(new Action(Action.Type.NO_ACTION), action);
    }

    @Test
    public void whenWaitPaeriodIsNotExpiredThenNoAction() throws Exception {
        when(this.timestampRepository.load()).thenReturn(null);

        final Action action = this.changesDetector.getActionForContent("a");

        assertEquals(new Action(Action.Type.NO_ACTION), action);
    }

}
