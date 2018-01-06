package gae.repository.cron;

import com.google.appengine.api.datastore.DatastoreService;
import gae.repository.Converter;
import gae.repository.Repository;
import service.cron.CronJobState;
import service.cron.CronJobStateRepository;

import java.util.List;

public class GaeCronJobStateRepository extends Repository<CronJobState> implements CronJobStateRepository {

    public GaeCronJobStateRepository(final DatastoreService service, final Converter<CronJobState> converter) {
        super(service, converter);
    }

    @Override
    public CronJobState load() {
        final List<CronJobState> values = this.loadAll();

        return values.isEmpty() ? null : values.get(0);
    }

}
