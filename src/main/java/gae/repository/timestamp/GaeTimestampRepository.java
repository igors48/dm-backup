package gae.repository.timestamp;

import com.google.appengine.api.datastore.DatastoreService;
import gae.repository.Converter;
import gae.repository.Repository;
import service.TimestampRepository;

import java.util.List;

/**
 * Created by igor on 28.11.2016.
 */
public class GaeTimestampRepository extends Repository<Long> implements TimestampRepository {

    public GaeTimestampRepository(final DatastoreService service, final Converter<Long> converter) {
        super(service, converter);
    }

    @Override
    public Long load() {
        final List<Long> values = this.loadAll();

        return values.isEmpty() ? null : values.get(0);
    }

}
