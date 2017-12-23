package service.cron;

import static util.Assert.guard;
import static util.Parameter.isPositive;

public class CronJobState {

    private long lastDailyBackupTimestamp;
    private int errorCounter;
    private int totalSuccessCount;
    private int totalFailCount;
    private int totalErrorCount;

    public CronJobState(final long lastDailyBackupTimestamp, final int errorCounter, final int totalSuccessCount, final int totalFailCount, final int totalErrorCount) {
        guard(isPositive(this.lastDailyBackupTimestamp = lastDailyBackupTimestamp));
        guard(isPositive(this.errorCounter = errorCounter));
        guard(isPositive(this.totalSuccessCount = totalSuccessCount));
        guard(isPositive(this.totalFailCount = totalFailCount));
        guard(isPositive(this.totalErrorCount = totalErrorCount));
    }

    public long getLastDailyBackupTimestamp() {
        return lastDailyBackupTimestamp;
    }

    public void onSuccess() {
        this.errorCounter = 0;
        ++this.totalSuccessCount;
    }

    public int onFail() {
        this.totalFailCount++;

        return ++this.errorCounter;
    }

    public void onError() {
        this.errorCounter = 0;
        ++this.totalErrorCount;
    }

    public int getTotalFailCount() {
        return this.totalFailCount;
    }

    public int getErrorCounter() {
        return this.errorCounter;
    }

    public int getTotalSuccessCount() {
        return this.totalSuccessCount;
    }

    public int getTotalErrorCount() {
        return this.totalErrorCount;
    }

}
