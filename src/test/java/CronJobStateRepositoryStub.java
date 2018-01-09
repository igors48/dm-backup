import service.cron.CronJobState;
import service.cron.CronJobStateRepository;

public class CronJobStateRepositoryStub implements CronJobStateRepository {

    private CronJobState state;

    public CronJobStateRepositoryStub(final CronJobState state) {
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
