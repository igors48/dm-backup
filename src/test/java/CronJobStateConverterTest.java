import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import gae.repository.EntityFactory;
import gae.repository.Kind;
import gae.repository.cron.CronJobStateConverter;
import org.junit.Before;
import org.junit.Test;
import service.cron.CronJobState;

import static org.junit.Assert.assertEquals;

public class CronJobStateConverterTest {

    private static final Key SAMPLE_KEY = KeyFactory.stringToKey("ag9zfnJzcy1jb2xsZWN0b3JyHQsSEEZlZWRIZWFkZXJFbnRpdHkYgICAgIi0vwgM");

    private CronJobStateConverter converter;

    @Before
    public void setUp() throws Exception {
        final EntityFactory<CronJobState> entityFactory = new EntityFactory<CronJobState>() {
            @Override
            public Entity createFor(final CronJobState data) {
                return new Entity(Kind.CRON_JOB_STATE.value, SAMPLE_KEY);
            }
        };

        this.converter = CronJobStateConverter.create(entityFactory);
    }

    @Test
    public void smoke() {
        final CronJobState original = TestData.createCronJobState();
        final Entity converted = this.converter.convert(original);
        final CronJobState restored = this.converter.convert(converted);

        assertEquals(original, restored);
    }

}
