import org.junit.Test;
import service.Action;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by igor on 04.12.2016.
 */
public class ChangesDetectorContentChangedTest extends ChangesDetectorTestBase {

    @Test
    public void whenNoPreviousSnapshotThenContentUpdated() throws Exception {
        when(this.snapshotRepository.loadLatestSnapshot()).thenReturn(null);

        final Action action = this.changesDetector.getActionForContent("b");

        assertEquals(new Action(Action.Type.UPDATE_LAST), action);
    }

    @Test
    public void whenContentChangedThenTimestampUpdated() throws Exception {
        this.changesDetector.getActionForContent("b\r\na");

        verify(this.timestampRepository).store(CURRENT_TIMESTAMP);
    }

    @Test
    public void whenNoPreviouslyStoredTimeStampThenContentSaved() throws Exception {
        when(this.timestampRepository.load()).thenReturn(null);

        final Action action = this.changesDetector.getActionForContent("b\r\na");

        assertEquals(new Action(Action.Type.SAVE), action);
    }

    @Test
    public void whenPreviouslyStoredTimeStampExistsThenContentUpdated() throws Exception {
        when(this.timestampRepository.load()).thenReturn(48L);

        final Action action = this.changesDetector.getActionForContent("b\r\na");

        assertEquals(new Action(Action.Type.UPDATE_LAST), action);
    }

}
