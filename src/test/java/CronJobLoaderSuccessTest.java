import org.junit.Test;
import service.cron.CronJobState;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class CronJobLoaderSuccessTest extends CronJobTestBase {

    @Test
    public void whenLoaderSuccessThenErrorCounterReset() throws Exception {
        when(this.loader.load()).thenReturn(CONTENT);

        final CronJobState original = new CronJobState(0, 1, 0, 0, 0);
        this.cronJobStateStore.store(original);

        this.cronJob.execute();

        final CronJobState updated = this.cronJobStateStore.load();

        assertEquals(0, updated.getErrorCounter());
    }

    @Test
    public void whenLoaderSuccessThenTotalSuccessCounterUpdated() throws Exception {
        when(this.loader.load()).thenReturn(CONTENT);

        final CronJobState original = new CronJobState(0, 1, 0, 0, 0);
        this.cronJobStateStore.store(original);

        this.cronJob.execute();

        final CronJobState updated = this.cronJobStateStore.load();

        assertEquals(1, updated.getTotalSuccessCount());
    }

}
