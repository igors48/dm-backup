package service.cron;

import com.google.appengine.api.datastore.Transaction;
import org.joda.time.DateTime;
import service.*;
import service.error.ServiceException;

import java.util.logging.Level;
import java.util.logging.Logger;

import static util.Assert.guard;
import static util.Parameter.notNull;
import static util.TransactionTools.rollbackIfActive;

public class CronJob {

    private static final Logger LOGGER = Logger.getLogger(CronJob.class.getName());

    private final CronJobConfiguration configuration;
    private final Loader loader;
    private final Sender sender;
    private final Backup backup;
    private final CronJobStateRepository cronJobStateRepository;
    private final TimeService timeService;
    private final Transactions transactions;

    public CronJob(final CronJobConfiguration configuration, final Loader loader, final Sender sender, final Backup backup, final CronJobStateRepository cronJobStateRepository, final TimeService timeService, final Transactions transactions) {
        guard(notNull(this.configuration = configuration));
        guard(notNull(this.loader = loader));
        guard(notNull(this.sender = sender));
        guard(notNull(this.backup = backup));
        guard(notNull(this.cronJobStateRepository = cronJobStateRepository));
        guard(notNull(this.timeService = timeService));
        guard(notNull(this.transactions = transactions));
    }

    public void execute() {
        try {
            final Content content = this.loader.load();
            onSuccessfulLoad(content);
        } catch (ServiceException e) {
            onFailedLoad(e);
        }
    }

    private void onFailedLoad(final ServiceException exception) {
        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final CronJobState cronJobState = this.loadCronJobState();
            final boolean errorMailShouldSent = this.checkErrorMailShouldSent(cronJobState);
            this.cronJobStateRepository.store(cronJobState);

            transaction.commit();

            if (errorMailShouldSent) {
                sendException(exception);
            }

        } finally {
            rollbackIfActive(transaction);
        }
    }

    private void sendException(final ServiceException exception) {

        try {
            this.sender.sendException(this.configuration.recipients.adminRecipient, exception);
        } catch (ServiceException e) {
            LOGGER.log(Level.SEVERE, "Error mail do not send", e);
        }

    }

    private boolean checkErrorMailShouldSent(final CronJobState cronJobState) {
        final long errorCounter = cronJobState.onFail();

        if (errorCounter > this.configuration.maxConsecutiveErrorsCount) {
            cronJobState.onError();
            return true;
        } else {
            return false;
        }
    }

    private void onSuccessfulLoad(final Content content) {
        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final CronJobState cronJobState = this.loadCronJobState();
            cronJobState.onSuccess();

            final long lastRushTime = this.calculateLastRushTime();
            final long lastDailyBackupTimestamp = cronJobState.getLastDailyBackupTimestamp();

            final boolean dailyBackupPerformed = lastRushTime < lastDailyBackupTimestamp;

            final boolean updateLastBackupTimeStamp = !dailyBackupPerformed;

            if (updateLastBackupTimeStamp) {
                final long timestamp = this.timeService.currentTimestamp();
                cronJobState.onDailyBackup(timestamp);
            }

            this.cronJobStateRepository.store(cronJobState);

            transaction.commit();

            if (dailyBackupPerformed) {
                this.backup.checkChanges(content);
            } else {
                this.backup.dailyBackup(content);
            }

        } finally {
            rollbackIfActive(transaction);
        }
    }

    private long calculateLastRushTime() {
        final long currentTimestamp = this.timeService.currentTimestamp();

        final DateTime current = new DateTime(currentTimestamp);

        final int year = current.getYear();
        final int month = current.getMonthOfYear();
        final int day = current.getDayOfMonth();

        final DateTime todayRushTime = new DateTime(year, month, day, this.configuration.rushHour, 0);

        final DateTime rushTime = currentTimestamp < todayRushTime.getMillis() ? todayRushTime.minusDays(1) : todayRushTime;

        return rushTime.getMillis();
    }

    private CronJobState loadCronJobState() {
        final CronJobState loaded = this.cronJobStateRepository.load();

        return loaded == null ? CronJobState.INITIAL : loaded;
    }

}
