import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import gae.repository.EntityFactory;
import gae.repository.Kind;
import gae.repository.timestamp.TimestampConverter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by igor on 03.12.2016.
 */
public class TimestampConverterTest {

    private static final Key SAMPLE_KEY = KeyFactory.stringToKey("ag9zfnJzcy1jb2xsZWN0b3JyHQsSEEZlZWRIZWFkZXJFbnRpdHkYgICAgIi0vwgM");

    private TimestampConverter converter;

    @Before
    public void setUp() throws Exception {
        final EntityFactory<Long> entityFactory = new EntityFactory<Long>() {
            @Override
            public Entity createFor(final Long data) {
                return new Entity(Kind.TIMESTAMP.value, SAMPLE_KEY);
            }
        };

        this.converter = TimestampConverter.create(entityFactory);
    }

    @Test
    public void smoke() throws Exception {
        final long original = 42;
        final Entity converted = this.converter.convert(original);
        final long restored = this.converter.convert(converted);

        assertEquals(original, restored);
    }

}
