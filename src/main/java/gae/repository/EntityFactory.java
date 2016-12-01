package gae.repository;

import com.google.appengine.api.datastore.Entity;

/**
 * Created by igor on 01.12.2016.
 */
public interface EntityFactory<T> {

    Entity createFor(T data);

}
