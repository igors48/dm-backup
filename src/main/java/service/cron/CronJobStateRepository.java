package service.cron;

public interface CronJobStateRepository {

    CronJobState load();

    void store(final CronJobState cronJobState);

}
