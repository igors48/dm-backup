package gae;

import gae.repository.snapshot.GaeSnapshotRepository;
import gae.repository.timestamp.GaeTimestampRepository;
import gae.repository.GaeTransactions;
import gae.service.GaeTimeService;
import service.*;
import service.configuration.Configuration;
import service.error.ServiceException;

/**
 * Created by igor on 20.11.2016.
 */
public enum Dependencies {

    GET;

    public Backup backupService() throws ServiceException {
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
