package gae.repository.snapshot;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;
import com.google.gson.Gson;
import gae.repository.Converter;
import gae.repository.EntityFactory;
import gae.repository.GaeDatastoreTools;
import gae.repository.Kind;
import service.Content;
import service.Snapshot;
import service.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 30.11.2016.
 */
public class SnapshotConverter extends Converter<Snapshot> {

    public static final SnapshotConverter CHANGES_SNAPSHOT_CONVERTER = SnapshotConverter.createForChanges();
    public static final SnapshotConverter DAILY_SNAPSHOT_CONVERTER = SnapshotConverter.createForDaily();

    private static final Gson GSON = new Gson();

    private static final long VERSION = 1;

    private static final String UUID_KEY = "uuid";
    private static final String TIMESTAMP_KEY = "timestamp";
    private static final String TYPE_KEY = "type";
    private static final String CONTENT_KEY = "content";

    private static class Writer implements gae.repository.Writer<Snapshot> {

        @Override
        public void write(final Entity entity, final Snapshot data) {
            guard(notNull(data));

            entity.setProperty(UUID_KEY, data.uuid.toString());
            entity.setProperty(TYPE_KEY, data.type.toString());
            entity.setProperty(TIMESTAMP_KEY, data.timestamp);

            final String contentAsString = GSON.toJson(data.content);
            final Text contentAsText = new Text(contentAsString);
            entity.setProperty(CONTENT_KEY, contentAsText);

            addReadableTimeStamp(data.timestamp, entity);
        }

    }

    private static class Reader implements gae.repository.Reader<Snapshot> {

        @Override
        public Snapshot read(final Entity entity) {
            guard(notNull(entity));

            final UUID uuid = UUID.fromString((String) entity.getProperty(UUID_KEY));
            final Type type = Type.valueOf((String) entity.getProperty(TYPE_KEY));
            final long timestamp = (long) entity.getProperty(TIMESTAMP_KEY);
            final Text contentAsText = (Text) entity.getProperty(CONTENT_KEY);
            final String contentAsString = contentAsText.getValue();
            final Content content = GSON.fromJson(contentAsString, Content.class);

            return new Snapshot(uuid, type, timestamp, content);
        }

    }

    private SnapshotConverter(final long version, final Kind kind, final Map<Long, gae.repository.Reader<Snapshot>> readers, final gae.repository.Writer<Snapshot> writer, final EntityFactory<Snapshot> entityFactory) {
        super(version, kind, readers, writer, entityFactory);
    }

    private static SnapshotConverter createForChanges() {
        final EntityFactory<Snapshot> entityFactory = new EntityFactory<Snapshot>() {
            @Override
            public Entity createFor(final Snapshot data) {
                final Key key = GaeDatastoreTools.createEntityKey(data.uuid.toString(), Kind.CHANGE);

                return new Entity(Kind.CHANGE.value, key);
            }
        };

        return create(Kind.CHANGE, entityFactory);
    }

    private static SnapshotConverter createForDaily() {
        final EntityFactory<Snapshot> entityFactory = new EntityFactory<Snapshot>() {
            @Override
            public Entity createFor(final Snapshot data) {
                final Key key = GaeDatastoreTools.createEntityKey(data.uuid.toString(), Kind.DAILY);

                return new Entity(Kind.DAILY.value, key);
            }
        };

        return create(Kind.DAILY, entityFactory);
    }

    public static SnapshotConverter create(final Kind kind, final EntityFactory<Snapshot> entityFactory) {
        guard(notNull(kind));
        guard(notNull(entityFactory));

        final Map<Long, gae.repository.Reader<Snapshot>> readers = new HashMap<>();
        readers.put(VERSION, new Reader());

        return new SnapshotConverter(VERSION, kind, readers, new Writer(), entityFactory);
    }

}
