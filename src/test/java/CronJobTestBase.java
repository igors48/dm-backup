import org.junit.Before;
import service.Loader;
import service.cron.CronJob;
import service.cron.CronJobConfiguration;
import service.cron.CronJobStateRepository;
import service.error.ServiceException;

import static org.mockito.Mockito.mock;

public class CronJobTestBase extends BackupTestBase {

    static final ServiceException SERVICE_EXCEPTION = mock(ServiceException.class);

    CronJobConfiguration configuration;
    CronJobStateRepository cronJobStateRepository;
    Loader loader;
    CronJob cronJob;

    @Before
    public void setUp() {
        super.setUp();

        this.configuration = new CronJobConfiguration(1, 1, this.recipients);
        this.cronJobStateRepository = new CronJobStateRepositoryStub(null);
        this.loader = mock(Loader.class);

        this.cronJob = new CronJob(this.configuration, this.loader, this.sender, this.backup, this.cronJobStateRepository, this.timeServiceStub, this.transactions);
    }
}
