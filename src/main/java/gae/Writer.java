package gae;

import com.google.appengine.api.datastore.Entity;

/**
 * Created by igor on 27.11.2016.
 */
public interface Writer<T> {

    Entity write(T entity);

}
