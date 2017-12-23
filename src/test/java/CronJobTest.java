import org.junit.Before;
import org.junit.Test;
import service.Loader;
import service.cron.CronJob;
import service.cron.CronJobConfiguration;
import service.cron.CronJobState;
import service.cron.CronJobStateStore;
import service.error.ServiceException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CronJobTest extends BackupTestBase {

    //TODO split tests

    private static final ServiceException SERVICE_EXCEPTION = mock(ServiceException.class);

    private CronJobConfiguration configuration;
    private CronJobStateStore cronJobStateStore;
    private Loader loader;

    private CronJob cronJob;

    @Before
    public void setUp() {
        super.setUp();

        this.configuration = new CronJobConfiguration(1, 1, 1, 1, this.recipients);
        this.cronJobStateStore = new CronJobStateStoreStub();
        this.loader = mock(Loader.class);

        this.cronJob = new CronJob(this.configuration, this.loader, this.sender, this.backup, this.cronJobStateStore, this.timeServiceStub, this.transactions);
    }

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
    public void whenLoaderSuccessThenTotalErrorCounterReset() throws Exception {
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

}
