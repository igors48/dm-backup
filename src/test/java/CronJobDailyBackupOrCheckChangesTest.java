import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import service.*;
import service.configuration.Recipients;
import service.cron.CronJob;
import service.cron.CronJobConfiguration;
import service.cron.CronJobState;
import service.cron.CronJobStateStore;
import service.error.ServiceException;
import util.account.Account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.*;

@RunWith(value = Parameterized.class)
public class CronJobDailyBackupOrCheckChangesTest {

    private enum Action {
        DAILY_BACKUP,
        CHECK_CHANGES
    }

    private static final Content CONTENT = new Content(new ArrayList<Account>(), "content");
    private static final int RUSH_HOUR = 22;

    private Backup backup;
    private CronJob cronJob;
    private Action expected;

    public CronJobDailyBackupOrCheckChangesTest(final String name, final String lastBackupDate, final String currentDate, final Action expected) throws ServiceException {
        final TimeService timeServiceStub = new TimeServiceStub(parse(currentDate));

        final CronJobState cronJobState = new CronJobState(parse(lastBackupDate), 0, 0, 0, 0);
        final CronJobStateStore cronJobStateStore = new CronJobStateStoreStub(cronJobState);

        final Sender sender = mock(Sender.class);

        final Loader loader = mock(Loader.class);
        when(loader.load()).thenReturn(CONTENT);

        this.backup = mock(Backup.class);

        final Transactions transactions = mock(Transactions.class);
        final TransactionStub transactionStub = new TransactionStub();
        when(transactions.beginOne()).thenReturn(transactionStub);

        final CronJobConfiguration configuration = new CronJobConfiguration(1, RUSH_HOUR, new Recipients("", new ArrayList<String>()));
        this.cronJob = new CronJob(configuration, loader, sender, this.backup, cronJobStateStore, timeServiceStub, transactions);

        this.expected = expected;
    }

    private static long parse(String date) {
        return DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss").parseDateTime(date).getMillis(); //04/02/2011 20:27:05
    }

    @Test
    public void test() {
        this.cronJob.execute();

        switch (this.expected) {

            case DAILY_BACKUP:
                verify(this.backup).dailyBackup(CONTENT);
                verifyNoMoreInteractions(this.backup);
                break;
            case CHECK_CHANGES:
                verify(this.backup).checkChanges(CONTENT);
                verifyNoMoreInteractions(this.backup);
                break;
        }
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[][]{

                }
        );
    }

}