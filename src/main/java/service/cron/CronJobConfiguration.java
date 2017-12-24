package service.cron;

import service.configuration.Recipients;

import static util.Assert.guard;
import static util.Parameter.isPositive;
import static util.Parameter.notNull;

public class CronJobConfiguration {

    public final int maxConsecutiveErrorsCount;
    public final int rushHour;

    public final Recipients recipients;

    public CronJobConfiguration(final int maxConsecutiveErrorsCount, final int rushHour, final Recipients recipients) {
        guard(isPositive(this.maxConsecutiveErrorsCount = maxConsecutiveErrorsCount));
        guard(isPositive(this.rushHour = rushHour));

        guard(notNull(this.recipients = recipients));
    }

}
