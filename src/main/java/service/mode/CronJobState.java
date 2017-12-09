package service.mode;

public class CronJobState {

    private long lastDailyBackupTimestamp;
    private int errorCounter;
    private int totalSuccessCount;
    private int totalFailCount;
    private int totalErrorCount;

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

}
