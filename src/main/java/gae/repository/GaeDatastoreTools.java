package gae.repository;

import com.google.appengine.api.datastore.*;

import java.util.List;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static java.lang.Integer.MAX_VALUE;
import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 15.10.13
 */
public class GaeDatastoreTools {

    private static final Key ROOT_KEY = KeyFactory.createKey(Kind.ROOT.value, Kind.ROOT.value);

    public static Key createEntityKey(final String uuid, final Kind kind) {
        guard(notNull(uuid));
        guard(notNull(kind));

        return KeyFactory.createKey(ROOT_KEY, kind.value, uuid);
    }

    public static Entity loadEntity(final String uuid, final Kind kind, final boolean keysOnly) {
        return loadEntity(uuid, Kind.ROOT, kind, keysOnly);
    }

    public static Entity loadEntity(final String uuid, final Kind rootKind, final Kind kind, final boolean keysOnly) {
        guard(notNull(uuid));
        guard(notNull(rootKind));
        guard(notNull(kind));

        final PreparedQuery preparedQuery = prepareQuery(uuid, rootKind, kind, keysOnly);

        return preparedQuery.asSingleEntity();
    }

    public static List<Entity> loadEntities(final Kind kind) {
        guard(notNull(kind));

        final Query query = new Query(kind.value);
        final PreparedQuery preparedQuery = GaeDatastore.INSTANCE.getDatastoreService().prepare(query);

        return preparedQuery.asList(withLimit(MAX_VALUE));
    }

    public static void deleteEntity(final String uuid, final Kind kind) {
        deleteEntity(uuid, Kind.ROOT, kind);
    }

    public static void deleteEntity(final String uuid, final Kind rootKind, final Kind kind) {
        guard(notNull(uuid));
        guard(notNull(rootKind));
        guard(notNull(kind));

        final Entity victim = loadEntity(uuid, rootKind, kind, true);

        if (victim != null) {
            GaeDatastore.INSTANCE.getDatastoreService().delete(victim.getKey());
        }
    }

    private static PreparedQuery prepareQuery(final String uuid, final Kind rootKind, final Kind kind, final boolean keysOnly) {
        final Key rootKey = createEntityKey(uuid, rootKind);
        final Query query = new Query(kind.value).setAncestor(rootKey);

        if (keysOnly) {
            query.setKeysOnly();
        }

        return GaeDatastore.INSTANCE.getDatastoreService().prepare(query);
    }

    private GaeDatastoreTools() {
        // empty
    }

}
