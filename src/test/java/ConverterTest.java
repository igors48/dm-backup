import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import gae.repository.Converter;
import gae.repository.EntityFactory;
import gae.repository.Reader;
import gae.repository.Writer;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by igor on 01.12.2016.
 */
public class ConverterTest {

    private static final long VERSION = 2;
    private static final Key SAMPLE_KEY = KeyFactory.stringToKey("ag9zfnJzcy1jb2xsZWN0b3JyHQsSEEZlZWRIZWFkZXJFbnRpdHkYgICAgIi0vwgM");
    private static final String PROPERTY_KEY = "property";
    private static final int INT_FROM_DUMMY_READER = 42;

    private Converter<Integer> converter;

    @Before
    public void setUp() throws Exception {
        final Writer<Integer> writer = new Writer<Integer>() {
            @Override
            public void write(final Entity entity, final Integer data) {
                entity.setProperty(PROPERTY_KEY, data);
            }
        };

        final Reader<Integer> reader01 = new Reader<Integer>() {
            @Override
            public Integer read(final Entity entity) {
                return INT_FROM_DUMMY_READER;
            }
        };

        final Reader<Integer> reader02 = new Reader<Integer>() {
            @Override
            public Integer read(final Entity entity) {
                return (Integer) entity.getProperty(PROPERTY_KEY);
            }
        };

        final Map<Long, Reader<Integer>> readers = new HashMap<>();
        readers.put(1L, reader01);
        readers.put(2L, reader02);

        final EntityFactory<Integer> entityFactory = new EntityFactory<Integer>() {
            @Override
            public Entity createFor(final Integer data) {
                return new Entity("integer", SAMPLE_KEY);
            }
        };

        this.converter = new Converter<>(VERSION, readers, writer, entityFactory);
    }

    @Test
    public void versionIdAddedToEntity() throws Exception {
        final Entity converted = this.converter.convert(48);
        final long version = (long) converted.getProperty(Converter.VERSION_KEY);

        assertEquals(VERSION, version);
    }

    @Test
    public void originalDataEqualsRestored() throws Exception {
        final int original = 84;
        final Entity converted = this.converter.convert(original);
        final int restored = this.converter.convert(converted);

        assertEquals(original, restored);
    }

    @Test
    public void readerChoosenByEntityVersion() throws Exception {
        final Entity converted = this.converter.convert(84);

        converted.setProperty(Converter.VERSION_KEY, 1L);

        final int restored = this.converter.convert(converted);

        assertEquals(INT_FROM_DUMMY_READER, restored);
    }

    @Test
    public void whenReadableTimestampAddedItIsPutInEntity() throws Exception {
        final Entity entity = this.converter.convert(84);
        Converter.addReadableTimeStamp(1488305961538L, entity);

        final String property = (String) entity.getProperty(Converter.READABLE_TIMESTAMP_KEY);

        assertEquals("2017-02-28 20:19:21", property);
    }

}
