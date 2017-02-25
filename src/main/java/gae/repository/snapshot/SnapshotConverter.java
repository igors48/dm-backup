package gae.repository.snapshot;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.repackaged.com.google.gson.Gson;
import gae.repository.Converter;
import gae.repository.EntityFactory;
import gae.repository.GaeDatastoreTools;
import gae.repository.Kind;
import service.Content;
import service.Snapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 30.11.2016.
 */
public class SnapshotConverter extends Converter<Snapshot> {

    public static final SnapshotConverter SNAPSHOT_CONVERTER = SnapshotConverter.create();

    private static final Gson GSON = new Gson();

    private static final int VERSION = 1;

    private static final String UUID_KEY = "uuid";
    private static final String TIMESTAMP_KEY = "timestamp";
    private static final String CONTENT_KEY = "content";

    private static class Writer implements gae.repository.Writer<Snapshot> {

        @Override
        public void write(final Entity entity, final Snapshot data) {
            guard(notNull(data));

            entity.setProperty(UUID_KEY, data.uuid.toString());
            entity.setProperty(TIMESTAMP_KEY, data.timestamp);

            final String contentAsString = GSON.toJson(data.content);
            final Text contentAsText = new Text(contentAsString);
            entity.setProperty(CONTENT_KEY, contentAsText);
        }

    }

    private static class Reader implements gae.repository.Reader<Snapshot> {

        @Override
        public Snapshot read(final Entity entity) {
            guard(notNull(entity));

            final UUID uuid = UUID.fromString((String) entity.getProperty(UUID_KEY));
            final long timestamp = (long) entity.getProperty(TIMESTAMP_KEY);
            final Text contentAsText = (Text) entity.getProperty(CONTENT_KEY);
            final String contentAsString = contentAsText.getValue();
            final Content content = GSON.fromJson(contentAsString, Content.class);

            return new Snapshot(uuid, timestamp, content);
        }

    }

    private SnapshotConverter(final int version, final Map<Integer, gae.repository.Reader<Snapshot>> readers, final gae.repository.Writer<Snapshot> writer, final EntityFactory<Snapshot> entityFactory) {
        super(version, readers, writer, entityFactory);
    }

    private static SnapshotConverter create() {
        final EntityFactory<Snapshot> entityFactory = new EntityFactory<Snapshot>() {
            @Override
            public Entity createFor(final Snapshot data) {
                final Key key = GaeDatastoreTools.createEntityKey(data.uuid.toString(), Kind.SNAPSHOT);

                return new Entity(Kind.SNAPSHOT.value, key);
            }
        };

        return create(entityFactory);
    }

    public static SnapshotConverter create(final EntityFactory<Snapshot> entityFactory) {
        guard(notNull(entityFactory));

        final Map<Integer, gae.repository.Reader<Snapshot>> readers = new HashMap<>();
        readers.put(VERSION, new Reader());

        return new SnapshotConverter(VERSION, readers, new Writer(), entityFactory);
    }

}
