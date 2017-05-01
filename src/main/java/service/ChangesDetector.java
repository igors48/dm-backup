package service;

import com.google.appengine.api.datastore.Transaction;
import util.account.Account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static util.Assert.guard;
import static util.Parameter.*;
import static util.TransactionTools.rollbackIfActive;

/**
 * Created by igor on 25.11.2016.
 */
public class ChangesDetector {

    private final SnapshotRepository changesSnapshotRepository;
    private final TimestampRepository timestampRepository;
    private final TimeService timeService;
    private final Transactions transactions;
    private final long waitInMillis;

    public ChangesDetector(final SnapshotRepository changesSnapshotRepository, final TimestampRepository timestampRepository, final TimeService timeService, final long waitInMillis, final Transactions transactions) {
        guard(notNull(this.changesSnapshotRepository = changesSnapshotRepository));
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

            final Snapshot latestSnapshot = this.changesSnapshotRepository.loadLatestSnapshot();

            final boolean contentChanged;

            if (latestSnapshot == null) {
                contentChanged = true;
            } else {
                final String latestContent = latestSnapshot.content.file;

                contentChanged = isContentChanged(latestContent, content);
            }

            final Action action;

            if (contentChanged) {
                action = createActionForChangedContent();
            } else {
                action = createActionForNotChangedContent();
            }

            transaction.commit();

            return action;
        } finally {
            rollbackIfActive(transaction);
        }
    }

    private Action createActionForNotChangedContent() {
        final Long stored = this.timestampRepository.load();

        if (stored == null) {
            return Action.NO_ACTION;
        }

        final long current = this.timeService.currentTimestamp();
        final long delta = current - stored;

        if (delta > this.waitInMillis) {
            this.timestampRepository.clear();

            final Snapshot preLatestSnapshot = this.changesSnapshotRepository.loadPreLatestSnapshot();
            final List<Account> accounts = preLatestSnapshot == null ? new ArrayList<Account>() : preLatestSnapshot.content.accounts;
            final Date before = preLatestSnapshot == null ? new Date() : new Date(preLatestSnapshot.timestamp);
            final Date after = new Date(current);

            return Action.send(accounts, before, after);
        }

        return Action.NO_ACTION;
    }

    private Action createActionForChangedContent() {
        final Long stored = this.timestampRepository.load();

        final long timestamp = this.timeService.currentTimestamp();
        this.timestampRepository.store(timestamp);

        return stored == null ? Action.SAVE : Action.UPDATE_LAST;
    }

    public static boolean isContentChanged(final String oldContent, final String newContent) {
        guard(isValidString(oldContent));
        guard(isValidString(newContent));

        final String oldOne = removeFirstLine(oldContent);
        final String newOne = removeFirstLine(newContent);

        return !(oldOne.equals(newOne));
    }

    private static String removeFirstLine(final String content) {
        final List<String> oldLines = new ArrayList<>(Arrays.asList(content.split("\\r?\\n")));
        oldLines.remove(0);

        final StringBuilder buffer = new StringBuilder();

        for (final String current : oldLines) {
            buffer.append(current);
        }

        return buffer.toString();
    }


}
