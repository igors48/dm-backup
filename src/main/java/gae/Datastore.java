package gae;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

/**
 * Created by igor on 27.11.2016.
 */
public enum Datastore {

    INSTANCE;

    private final DatastoreService datastoreService;

    private Datastore() {
        this.datastoreService = DatastoreServiceFactory.getDatastoreService();
    }

    public DatastoreService getDatastoreService() {
        return datastoreService;
    }

}
