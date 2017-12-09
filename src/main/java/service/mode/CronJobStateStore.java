package service.mode;

import static util.Assert.guard;
import static util.Parameter.notNull;

public class CronJobStateStore {

    public CronJobState load() {
        return null;
    }

    public void store(final CronJobState cronJobState) {
        guard(notNull(cronJobState));
    }

}
