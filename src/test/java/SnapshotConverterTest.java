import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import gae.repository.EntityFactory;
import gae.repository.Kind;
import gae.repository.snapshot.SnapshotConverter;
import org.junit.Before;
import org.junit.Test;
import service.Content;
import service.Snapshot;
import service.Type;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Created by igor on 03.12.2016.
 */
public class SnapshotConverterTest {

    private static final Key SAMPLE_KEY = KeyFactory.stringToKey("ag9zfnJzcy1jb2xsZWN0b3JyHQsSEEZlZWRIZWFkZXJFbnRpdHkYgICAgIi0vwgM");

    private SnapshotConverter converter;

    @Before
    public void setUp() throws Exception {
        final EntityFactory<Snapshot> entityFactory = new EntityFactory<Snapshot>() {
            @Override
            public Entity createFor(final Snapshot data) {
                return new Entity(Kind.CHANGE.value, SAMPLE_KEY);
            }
        };

        this.converter = SnapshotConverter.create(Kind.CHANGE, entityFactory);
    }

    @Test
    public void smoke() throws Exception {
        final Content content = TestData.createContent();
        final Snapshot original = new Snapshot(UUID.randomUUID(), Type.CHANGE, 48, content);
        final Entity converted = this.converter.convert(original);
        final Snapshot restored = this.converter.convert(converted);

        assertEquals(original, restored);
    }

}
