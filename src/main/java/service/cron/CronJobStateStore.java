package service.cron;

public interface CronJobStateStore {

    CronJobState load();

    void store(final CronJobState cronJobState);

}
