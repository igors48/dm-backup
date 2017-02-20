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

    public Action getActionForContent(final String content) {
        guard(isValidString(content));

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final Snapshot latestSnapshot = this.snapshotRepository.loadLatestSnapshot();

            final boolean contentChanged;

            if (latestSnapshot == null) {
                contentChanged = true;
            } else {
                final String latestContent = latestSnapshot.content.file;

                contentChanged = isContentChanged(latestContent, content);
            }

            final Action action = contentChanged ? updateStoredTimestamp() : checkWaitPeriodFinished();

            transaction.commit();

            return action;
        } finally {
            rollbackIfActive(transaction);
        }
    }

    private Action checkWaitPeriodFinished() {
        final Long stored = this.timestampRepository.load();

        if (stored == null) {
            return Action.NO_ACTION;
        }

        final long current = this.timeService.currentTimestamp();
        final long delta = current - stored;

        if (delta > this.waitInMillis) {
            this.timestampRepository.clear();

            return Action.SEND;
        }

        return Action.NO_ACTION;
    }

    private Action updateStoredTimestamp() {
        final Long stored = this.timestampRepository.load();

        final long timestamp = this.timeService.currentTimestamp();
        this.timestampRepository.store(timestamp);

        return stored == null ? Action.SAVE : Action.UPDATE_LAST;
    }

    private boolean isContentChanged(final String oldContent, final String newContent) {
        return !(oldContent.equals(newContent));
    }

}
