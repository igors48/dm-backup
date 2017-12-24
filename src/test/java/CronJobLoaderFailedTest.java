import org.junit.Test;
import service.cron.CronJobState;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

public class CronJobLoaderFailedTest extends CronJobTestBase {

    @Test
    public void whenLoaderFailedThenTotalFailCounterUpdated() throws Exception {
        doThrow(SERVICE_EXCEPTION).when(this.loader).load();

        final CronJobState original = new CronJobState(0, 0, 0, 0, 0);
        this.cronJobStateStore.store(original);

        this.cronJob.execute();

        final CronJobState updated = this.cronJobStateStore.load();

        assertEquals(1, updated.getTotalFailCount());
    }

    @Test
    public void whenLoaderFailedThenTotalErrorCounterUpdated() throws Exception {
        doThrow(SERVICE_EXCEPTION).when(this.loader).load();

        final CronJobState original = new CronJobState(0, 0, 0, 0, 0);
        this.cronJobStateStore.store(original);

        this.cronJob.execute();

        final CronJobState updated = this.cronJobStateStore.load();

        assertEquals(1, updated.getErrorCounter());
    }

    @Test
    public void whenLoaderConsecutiveErrorsCountLimitReachedThenErrorCounterReset() throws Exception {
        doThrow(SERVICE_EXCEPTION).when(this.loader).load();

        final CronJobState original = new CronJobState(0, this.configuration.maxConsecutiveErrorsCount, 0, 0, 0);
        this.cronJobStateStore.store(original);

        this.cronJob.execute();

        final CronJobState updated = this.cronJobStateStore.load();

        assertEquals(0, updated.getErrorCounter());
    }

    @Test
    public void whenLoaderConsecutiveErrorsCountLimitReachedThenTotalErrorCounterUpdated() throws Exception {
        doThrow(SERVICE_EXCEPTION).when(this.loader).load();

        final CronJobState original = new CronJobState(0, this.configuration.maxConsecutiveErrorsCount, 0, 0, 0);
        this.cronJobStateStore.store(original);

        this.cronJob.execute();

        final CronJobState updated = this.cronJobStateStore.load();

        assertEquals(1, updated.getTotalErrorCount());
    }

    @Test
    public void whenLoaderConsecutiveErrorsCountLimitReachedThenMailShouldBeSent() throws Exception {
        doThrow(SERVICE_EXCEPTION).when(this.loader).load();

        final CronJobState original = new CronJobState(0, this.configuration.maxConsecutiveErrorsCount, 0, 0, 0);
        this.cronJobStateStore.store(original);

        this.cronJob.execute();

        verify(this.sender).sendException(this.configuration.recipients.adminRecipient, SERVICE_EXCEPTION);
    }

}
