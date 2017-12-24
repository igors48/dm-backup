import service.cron.CronJobState;
import service.cron.CronJobStateStore;

public class CronJobStateStoreStub implements CronJobStateStore {

    private CronJobState state;

    public CronJobStateStoreStub(final CronJobState state) {
        this.state = state;
    }

    @Override
    public CronJobState load() {
        return this.state;
    }

    @Override
    public void store(final CronJobState cronJobState) {
        this.state = cronJobState;
    }

}
