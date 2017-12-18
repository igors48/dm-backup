package service.mode;

import com.google.appengine.api.datastore.Transaction;
import org.joda.time.DateTime;
import service.*;
import service.error.ServiceException;

import java.util.logging.Level;
import java.util.logging.Logger;

import static util.Assert.guard;
import static util.Parameter.notNull;
import static util.TransactionTools.rollbackIfActive;

public class Selector {

    private static final Logger LOGGER = Logger.getLogger(Selector.class.getName());

    private final Configuration configuration;
    private final Loader loader;
    private final Sender sender;
    private final CronJobStateStore cronJobStateStore;
    private final TimeService timeService;
    private final Transactions transactions;

    public Selector(final Configuration configuration, final Loader loader, final Sender sender, final CronJobStateStore cronJobStateStore, final TimeService timeService, final Transactions transactions) {
        guard(notNull(this.configuration = configuration));
        guard(notNull(this.loader = loader));
        guard(notNull(this.sender = sender));
        guard(notNull(this.cronJobStateStore = cronJobStateStore));
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

            final CronJobState cronJobState = this.cronJobStateStore.load();
            final boolean errorMailShouldSent = this.checkErrorMailShouldSent(cronJobState);
            this.cronJobStateStore.store(cronJobState);

            transaction.commit();

            if (errorMailShouldSent) {
                sendException(exception);
            }

        } finally {
            rollbackIfActive(transaction);
        }
    }

    private void sendException(ServiceException exception) {

        try {
            this.sender.sendException(this.configuration.recipients.adminRecipient, exception);
        } catch (ServiceException e) {
            LOGGER.log(Level.SEVERE, "Error mail do not send", e);
        }

    }

    private boolean checkErrorMailShouldSent(CronJobState cronJobState) {
        final int errorCounter = cronJobState.onFail();

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

            final CronJobState cronJobState = this.cronJobStateStore.load();
            cronJobState.onSuccess();

            final long lastRushTime = this.calculateLastRushTime();
            final boolean dailyBackupPerformed = lastRushTime < cronJobState.getLastDailyBackupTimestamp();

            this.cronJobStateStore.store(cronJobState);

            transaction.commit();

            if (dailyBackupPerformed) {
                this.checkChanges(content);
            } else {
                this.dailyBackup(content);
            }

        } catch (Exception e) {

        } finally {
            rollbackIfActive(transaction);
        }
    }

    private void dailyBackup(Content content) {
        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

        } finally {
            rollbackIfActive(transaction);
        }
    }

    private void checkChanges(Content content) {
        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

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

}
