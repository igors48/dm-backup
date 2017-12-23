import service.cron.CronJobState;
import service.cron.CronJobStateStore;

public class CronJobStateStoreStub implements CronJobStateStore {

    private CronJobState state;

    @Override
    public CronJobState load() {
        return this.state;
    }

    @Override
    public void store(final CronJobState cronJobState) {
        this.state = cronJobState;
    }

}
