package gae;

import com.google.appengine.api.datastore.Transaction;
import service.Transactions;

/**
 * @author : igu
 */
public enum GaeTransactions implements Transactions {

    INSTANCE;

    @Override
    public Transaction beginOne() {
        return Datastore.INSTANCE.getDatastoreService().beginTransaction();
    }

}
