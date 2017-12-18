package service.cron;

import service.configuration.Recipients;

import static util.Assert.guard;
import static util.Parameter.isPositive;
import static util.Parameter.notNull;

public class CronJobConfiguration {

    public final int backupPeriod;
    public final int maxConsecutiveErrorsCount;
    public final int rushHour;
    public final int rushPeriod;

    public final Recipients recipients;

    public CronJobConfiguration(final int backupPeriod, final int maxConsecutiveErrorsCount, final int rushHour, final int rushPeriod, final Recipients recipients) {
        guard(isPositive(this.backupPeriod = backupPeriod));
        guard(isPositive(this.maxConsecutiveErrorsCount = maxConsecutiveErrorsCount));
        guard(isPositive(this.rushHour = rushHour));
        guard(isPositive(this.rushPeriod = rushPeriod));

        guard(notNull(this.recipients = recipients));
    }

}
