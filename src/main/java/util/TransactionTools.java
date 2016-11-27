package util;

import com.google.appengine.api.datastore.Transaction;

/**
 * Created by igor on 26.11.2016.
 */
public final class TransactionTools {

    public static void rollbackIfActive(final Transaction transaction) {

        if (transaction != null) {

            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    private TransactionTools() {
        // empty
    }
}
