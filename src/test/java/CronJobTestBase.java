import org.junit.Before;
import service.Loader;
import service.cron.CronJob;
import service.cron.CronJobConfiguration;
import service.cron.CronJobStateStore;
import service.error.ServiceException;

import static org.mockito.Mockito.mock;

public class CronJobTestBase extends BackupTestBase {
    protected static final ServiceException SERVICE_EXCEPTION = mock(ServiceException.class);
    protected CronJobConfiguration configuration;
    protected CronJobStateStore cronJobStateStore;
    protected Loader loader;
    protected CronJob cronJob;

    @Before
    public void setUp() {
        super.setUp();

        this.configuration = new CronJobConfiguration(1, 1, 1, 1, this.recipients);
        this.cronJobStateStore = new CronJobStateStoreStub();
        this.loader = mock(Loader.class);

        this.cronJob = new CronJob(this.configuration, this.loader, this.sender, this.backup, this.cronJobStateStore, this.timeServiceStub, this.transactions);
    }
}
