package gae;

import gae.repository.GaeDatastore;
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
    private static SnapshotRepository changesSnapshotRepository;
    private static SnapshotRepository dailySnapshotRepository;
    private static TimestampRepository timestampRepository;
    private static TimeService timeService;
    private static ChangesDetector changesDetector;
    private static SnapshotStore changesSnapshotStore;
    private static SnapshotStore dailySnapshotStore;
    private static Backup backup;

    static {

        try {

            configuration = Configuration.fromSystemProperties();
            loader = new Loader(configuration.getAccessParameters());
            sender = new Sender(configuration.getAppVersion());
            transactions = GaeTransactions.INSTANCE;
            changesSnapshotRepository = new GaeSnapshotRepository(GaeDatastore.INSTANCE.getDatastoreService(), SnapshotConverter.CHANGES_SNAPSHOT_CONVERTER);
            dailySnapshotRepository = new GaeSnapshotRepository(GaeDatastore.INSTANCE.getDatastoreService(), SnapshotConverter.DAILY_SNAPSHOT_CONVERTER);
            timestampRepository = new GaeTimestampRepository(GaeDatastore.INSTANCE.getDatastoreService(), TimestampConverter.TIMESTAMP_CONVERTER);
            timeService = GaeTimeService.INSTANCE;
            changesSnapshotStore = new SnapshotStore(SNAPSHOTS_STORE_CAPACITY, Type.CHANGE, changesSnapshotRepository, timeService);
            dailySnapshotStore = new SnapshotStore(SNAPSHOTS_STORE_CAPACITY, Type.DAILY, dailySnapshotRepository, timeService);
            changesDetector = new ChangesDetector(changesSnapshotRepository, timestampRepository, timeService, configuration.getWaitTimeMillis(), transactions);
            backup = new Backup(loader, sender, changesDetector, configuration.getRecipients(), changesSnapshotStore, dailySnapshotStore, transactions);

        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Application initialization error", exception);
        }
    }

    public Backup backupService() {
        return backup;
    }

}
