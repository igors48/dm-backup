import org.junit.Before;
import org.junit.Test;
import service.Loader;
import service.cron.CronJob;
import service.cron.CronJobConfiguration;
import service.cron.CronJobStateStore;

import static org.mockito.Mockito.mock;

public class CronJobTest extends BackupTestBase {

    private CronJobConfiguration configuration;
    private CronJobStateStore cronJobStateStore;
    private Loader loader;

    private CronJob cronJob;

    @Before
    public void setUp() {
        super.setUp();

        this.configuration = new CronJobConfiguration(1, 1, 1, 1, this.recipients);
        this.cronJobStateStore = mock(CronJobStateStore.class);
        this.loader = mock(Loader.class);

        this.cronJob = new CronJob(this.configuration, this.loader, this.sender, this.backup, this.cronJobStateStore, this.timeServiceStub, this.transactions);
    }

    @Test
    public void name() {

    }
}
