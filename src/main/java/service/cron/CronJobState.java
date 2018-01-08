package service.cron;

import static util.Assert.guard;
import static util.Parameter.isPositive;

public class CronJobState {

    public static final CronJobState INITIAL = new CronJobState(0, 0, 0, 0, 0);

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

    public void onDailyBackup(final long timestamp) {
        guard(isPositive(this.lastDailyBackupTimestamp = timestamp));
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CronJobState that = (CronJobState) o;

        if (lastDailyBackupTimestamp != that.lastDailyBackupTimestamp) return false;
        if (errorCounter != that.errorCounter) return false;
        if (totalSuccessCount != that.totalSuccessCount) return false;
        if (totalFailCount != that.totalFailCount) return false;
        return totalErrorCount == that.totalErrorCount;
    }

    @Override
    public int hashCode() {
        int result = (int) (lastDailyBackupTimestamp ^ (lastDailyBackupTimestamp >>> 32));
        result = 31 * result + errorCounter;
        result = 31 * result + totalSuccessCount;
        result = 31 * result + totalFailCount;
        result = 31 * result + totalErrorCount;
        return result;
    }

}
