package gae.repository;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

/**
 * Created by igor on 27.11.2016.
 */
public enum GaeDatastore {

    //TODO consider move to dependencies
    INSTANCE;

    private final DatastoreService datastoreService;

    GaeDatastore() {
        this.datastoreService = DatastoreServiceFactory.getDatastoreService();
    }

    public DatastoreService getDatastoreService() {
        return datastoreService;
    }

}
