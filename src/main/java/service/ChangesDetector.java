package service;

import com.google.appengine.api.datastore.Transaction;

import static util.Assert.guard;
import static util.Parameter.*;
import static util.TransactionTools.rollbackIfActive;

/**
 * Created by igor on 25.11.2016.
 */
public class ChangesDetector {

    private final SnapshotRepository snapshotRepository;
    private final TimestampRepository timestampRepository;
    private final TimeService timeService;
    private final Transactions transactions;
    private final long waitInMillis;

    public ChangesDetector(final SnapshotRepository snapshotRepository, final TimestampRepository timestampRepository, final TimeService timeService, final long waitInMillis, final Transactions transactions) {
        guard(notNull(this.snapshotRepository = snapshotRepository));
        guard(notNull(this.timestampRepository = timestampRepository));
        guard(notNull(this.timeService = timeService));
        guard(notNull(this.transactions = transactions));
        guard(isPositive(this.waitInMillis = waitInMillis));
    }

    public boolean contentMustBeSent(final String content) {
        guard(isValidString(content));

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final String latestSnapshot = this.snapshotRepository.loadLatestSnapshot();
            final boolean contentChanged = isContentChanged(latestSnapshot, content);

            boolean contentMustBeSent = false;

            if (contentChanged) {
                updateStoredTimestamp();
            } else {
                contentMustBeSent = isWaitPeriodFinished();
            }

            transaction.commit();

            return contentMustBeSent;
        } finally {
            rollbackIfActive(transaction);
        }
    }

    private boolean isWaitPeriodFinished() {
        final Long stored = this.timestampRepository.load();

        if (stored == null) {
            return false;
        }

        final long current = this.timeService.currentTimestamp();
        final long delta = current - stored;

        if (delta > this.waitInMillis) {
            this.timestampRepository.clear();

            return true;
        }

        return false;
    }

    private void updateStoredTimestamp() {
        final long timestamp = this.timeService.currentTimestamp();
        this.timestampRepository.store(timestamp);
    }

    private boolean isContentChanged(final String oldContent, final String newContent) {
        return !(oldContent.equals(newContent));
    }

}
