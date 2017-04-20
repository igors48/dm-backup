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

        accounts.add(createAccount("t1", "1.00"));
        accounts.add(createAccount("t2", "2.00"));
        accounts.add(createAccount("t3", "3.00"));
        accounts.add(createAccount("t5", "5.00"));
        accounts.add(createAccount("t6", "7.00"));

        return accounts;
    }

    private static ArrayList<Account> createPreviousAccounts() {
        final ArrayList<Account> accounts = new ArrayList<>();

        accounts.add(createAccount("t1", "1.00"));
        accounts.add(createAccount("t2", "2.00"));
        accounts.add(createAccount("t4", "4.00"));
        accounts.add(createAccount("t5", "4.00"));
        accounts.add(createAccount("t6", "8.00"));

        return accounts;
    }

    private static Account createAccount(final String title, final String balance) {
        return new Account(title, balance);
    }

    private static void generateContentMail() {
        final String caption = "Caption for mail";
        final String time = "2017-03-02 20:15:08";
        final String server = "dm-backup";
        final List<Account> accounts = createAccounts();
        final List<Account> previousAccounts = new ArrayList<>();
        final String version = "1.2";

        final String content = Template.formatContent(caption, time, server, accounts, previousAccounts, version);

        try (PrintWriter out = new PrintWriter("C:\\Igor\\temp\\content.html")) {
            out.println(content);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void generateChangedContentMail() {
        final String caption = "Caption for mail";
        final String time = "2017-03-02 20:15:08";
        final String server = "dm-backup";
        final List<Account> accounts = createAccounts();
        final List<Account> previousAccounts = createPreviousAccounts();
        final String version = "1.2";

        final String content = Template.formatContent(caption, time, server, accounts, previousAccounts, version);

        try (PrintWriter out = new PrintWriter("C:\\Igor\\temp\\changed-content.html")) {
            out.println(content);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void generateErrorMail() {
        final String time = "2017-03-02 20:15:08";
        final String server = "dm-backup";
        final String description = "unexpected error";
        final String version = "1.2";

        final String content = Template.formatError(time, server, description, version);

        try (PrintWriter out = new PrintWriter("C:\\Igor\\temp\\error.html")) {
            out.println(content);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] arguments) {
        generateContentMail();
        generateChangedContentMail();
        generateErrorMail();
    }

}
