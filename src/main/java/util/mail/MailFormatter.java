package util.mail;

import service.Template;
import util.account.Account;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by igor on 03.03.2017.
 */
public class MailFormatter {

    private static ArrayList<Account> createAccounts() {
        final ArrayList<Account> accounts = new ArrayList<>();

        accounts.add(createAccount("t1", "b1"));
        accounts.add(createAccount("t2", "b2"));

        return accounts;
    }

    private static Account createAccount(final String title, final String balance) {
        return new Account(title, balance);
    }

    public static void main(String[] arguments) {
        final String caption = "Caption for mail";
        final String time = "2017-03-02 20:15:08";
        final String server = "dm-backup";
        final List<Account> accounts = createAccounts();
        final String version = "1.2";

        final String content = Template.formatContent(caption, time, server, accounts, version);

        try (PrintWriter out = new PrintWriter("C:\\Igor\\temp\\test.html")) {
            out.println(content);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}