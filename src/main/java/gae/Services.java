package gae;

import service.*;
import service.configuration.Configuration;
import service.error.ServiceException;

/**
 * Created by igor on 20.11.2016.
 */
public enum Services {

    INSTANCE;

    public Backup createBackupService() throws ServiceException {
        final Configuration configuration = Configuration.fromSystemProperties();

        final Loader loader = new Loader(configuration.getAccessParameters());
        final Sender sender = new Sender();
        final Transactions transactions = GaeTransactions.INSTANCE;
        final SnapshotRepository snapshotRepository = GaeSnapshotRepository.create();
        final TimestampRepository timestampRepository = GaeTimestampRepository.create();
        final TimeService timeService = GaeTimeService.INSTANCE;
        final ChangesDetector changesDetector = new ChangesDetector(snapshotRepository, timestampRepository, timeService, 48, transactions);

        return new Backup(loader, sender, changesDetector, configuration.getRecipients());
    }

}
