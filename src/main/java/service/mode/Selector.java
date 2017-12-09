package service.mode;

import com.google.appengine.api.datastore.Transaction;
import service.Content;
import service.Loader;
import service.Sender;
import service.Transactions;
import service.error.ServiceException;

import static util.Assert.guard;
import static util.Parameter.notNull;
import static util.TransactionTools.rollbackIfActive;

public class Selector {

    private final Configuration configuration;
    private final Loader loader;
    private final Sender sender;
    private final CronJobStateStore cronJobStateStore;
    private final Transactions transactions;

    public Selector(final Configuration configuration, final Loader loader, final Sender sender, final CronJobStateStore cronJobStateStore, final Transactions transactions) {
        guard(notNull(this.configuration = configuration));
        guard(notNull(this.loader = loader));
        guard(notNull(this.sender = sender));
        guard(notNull(this.cronJobStateStore = cronJobStateStore));
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

            }

        } catch (Exception e) {

        } finally {
            rollbackIfActive(transaction);
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

            transaction.commit();
        } catch (Exception e) {

        } finally {
            rollbackIfActive(transaction);
        }
    }

}
