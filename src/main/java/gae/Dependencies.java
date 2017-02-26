package gae;

import gae.repository.GaeTransactions;
import gae.repository.snapshot.GaeSnapshotRepository;
import gae.repository.snapshot.SnapshotConverter;
import gae.repository.timestamp.GaeTimestampRepository;
import gae.repository.timestamp.TimestampConverter;
import gae.service.GaeTimeService;
import service.*;
import service.configuration.Configuration;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by igor on 20.11.2016.
 */
public enum Dependencies {

    GET;

    private static final Logger LOGGER = Logger.getLogger(Dependencies.class.getName());

    private static final int SNAPSHOTS_STORE_CAPACITY = 10; //TODO move to properties

    private static Configuration configuration;
    private static Loader loader;
    private static Sender sender;
    private static Transactions transactions;
    private static SnapshotRepository snapshotRepository;
    private static TimestampRepository timestampRepository;
    private static TimeService timeService;
    private static ChangesDetector changesDetector;
    private static SnapshotStore snapshotStore;
    private static Backup backup;

    static {

        try {

            configuration = Configuration.fromSystemProperties();
            loader = new Loader(configuration.getAccessParameters());
            sender = new Sender(configuration.getAppVersion());
            transactions = GaeTransactions.INSTANCE;
            snapshotRepository = new GaeSnapshotRepository(SnapshotConverter.SNAPSHOT_CONVERTER);
            timestampRepository = new GaeTimestampRepository(TimestampConverter.TIMESTAMP_CONVERTER);
            timeService = GaeTimeService.INSTANCE;
            snapshotStore = new SnapshotStore(SNAPSHOTS_STORE_CAPACITY, Type.CHANGE, snapshotRepository, timeService);
            changesDetector = new ChangesDetector(snapshotRepository, timestampRepository, timeService, configuration.getWaitTimeMillis(), transactions);
            backup = new Backup(loader, sender, changesDetector, configuration.getRecipients(), snapshotStore, transactions);

        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Application initialization error", exception);
        }
    }

    public Backup backupService() {
        return backup;
    }

}
