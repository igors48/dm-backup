import service.Content;
import util.account.Account;

import java.util.ArrayList;

/**
 * Created by igor on 22.02.2017.
 */
public class TestData {

    public static Content createContent() {
        return new Content(createAccounts(), "a");
    }

    public static ArrayList<Account> createAccounts() {
        final ArrayList<Account> accounts = new ArrayList<>();

        accounts.add(createAccount("t1", "b1"));
        accounts.add(createAccount("t2", "b2"));

        return accounts;
    }

    public static Account createAccount(String title, String balance) {
        return new Account(title, balance);
    }


}
