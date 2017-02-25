package service;

import com.google.appengine.api.datastore.Transaction;

/**
 * Created by igor on 26.11.2016.
 */
public interface Transactions {

    Transaction beginOne();

}
