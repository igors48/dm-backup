package gae.repository.cron;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import gae.repository.Converter;
import gae.repository.EntityFactory;
import gae.repository.Kind;
import gae.repository.Repository;
import service.cron.CronJobState;

import java.util.HashMap;
import java.util.Map;

import static util.Assert.guard;
import static util.Parameter.notNull;

public class CronJobStateConverter extends Converter<CronJobState> {

    public static final CronJobStateConverter CRON_JOB_STATE_CONVERTER = CronJobStateConverter.create();

    private static final int VERSION = 1;

    private static final String LAST_DAILY_BACKUP_TIMESTAMP_KEY = "lastDailyBackupTimestamp";
    private static final String ERROR_COUNTER_KEY = "errorCounter";
    private static final String TOTAL_SUCCESS_COUNT_KEY = "totalSuccessCount";
    private static final String TOTAL_FAIL_COUNT_KEY = "totalFailCount";
    private static final String TOTAL_ERROR_COUNT_KEY = "totalErrorCount";

    private static class Writer implements gae.repository.Writer<CronJobState> {

        @Override
        public void write(final Entity entity, final CronJobState data) {
            guard(notNull(entity));
            guard(notNull(data));

            entity.setProperty(LAST_DAILY_BACKUP_TIMESTAMP_KEY, data.getLastDailyBackupTimestamp());
            entity.setProperty(ERROR_COUNTER_KEY, data.getErrorCounter());
            entity.setProperty(TOTAL_SUCCESS_COUNT_KEY, data.getTotalSuccessCount());
            entity.setProperty(TOTAL_FAIL_COUNT_KEY, data.getTotalFailCount());
            entity.setProperty(TOTAL_ERROR_COUNT_KEY, data.getTotalErrorCount());

            addReadableTimeStamp(data.getLastDailyBackupTimestamp(), entity);
        }

    }

    private static class Reader implements gae.repository.Reader<CronJobState> {

        @Override
        public CronJobState read(final Entity entity) {
            guard(notNull(entity));

            final long lastDailyBackupTimestamp = (long) entity.getProperty(LAST_DAILY_BACKUP_TIMESTAMP_KEY);
            final long errorCounter = (long) entity.getProperty(ERROR_COUNTER_KEY);
            final long totalSuccessCount = (long) entity.getProperty(TOTAL_SUCCESS_COUNT_KEY);
            final long totalFailCount = (long) entity.getProperty(TOTAL_FAIL_COUNT_KEY);
            final long totalErrorCount = (long) entity.getProperty(TOTAL_ERROR_COUNT_KEY);

            return new CronJobState(lastDailyBackupTimestamp, errorCounter, totalSuccessCount, totalFailCount, totalErrorCount);
        }

    }

    private CronJobStateConverter(final long version, final Map<Long, gae.repository.Reader<CronJobState>> readers, final gae.repository.Writer<CronJobState> writer, final EntityFactory<CronJobState> entityFactory) {
        super(version, Kind.CRON_JOB_STATE, readers, writer, entityFactory);
    }

    private static CronJobStateConverter create() {
        final EntityFactory<CronJobState> entityFactory = new EntityFactory<CronJobState>() {
            @Override
            public Entity createFor(final CronJobState data) {
                final Key key = Repository.createEntityKey(Kind.CRON_JOB_STATE.value, Kind.CRON_JOB_STATE);

                return new Entity(Kind.CRON_JOB_STATE.value, key);
            }
        };

        return create(entityFactory);
    }

    public static CronJobStateConverter create(final EntityFactory<CronJobState> entityFactory) {
        guard(notNull(entityFactory));

        final Map<Long, gae.repository.Reader<CronJobState>> readers = new HashMap<>();
        readers.put(1L, new CronJobStateConverter.Reader());

        return new CronJobStateConverter(VERSION, readers, new CronJobStateConverter.Writer(), entityFactory);
    }

}
