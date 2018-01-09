import service.Content;
import service.cron.CronJobState;
import util.account.Account;

import java.util.ArrayList;

/**
 * Created by igor on 22.02.2017.
 */
public class TestData {

    public static Content createContent(final String file) {
        return new Content(createAccounts(), file);
    }

    public static Content createContent() {
        return createContent("a");
    }

    public static ArrayList<Account> createAccounts() {
        final ArrayList<Account> accounts = new ArrayList<>();

        accounts.add(createAccount("t1", "b1"));
        accounts.add(createAccount("t2", "b2"));

        return accounts;
    }

    public static Account createAccount(final String title, final String balance) {
        return new Account(title, balance);
    }

    public static CronJobState createCronJobState() {
        return new CronJobState(48, 49, 50, 51, 52);
    }

}
