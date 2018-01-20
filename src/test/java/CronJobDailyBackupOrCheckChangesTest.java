import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import service.*;
import service.configuration.Recipients;
import service.cron.CronJob;
import service.cron.CronJobConfiguration;
import service.cron.CronJobState;
import service.cron.CronJobStateRepository;
import service.error.ServiceException;
import util.account.Account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(value = Parameterized.class)
public class CronJobDailyBackupOrCheckChangesTest {

    private enum Action {
        DAILY_BACKUP,
        CHECK_CHANGES
    }

    private static final String EMAIL = "admin@a.com";
    private static final Content CONTENT = new Content(new ArrayList<Account>(), "content");
    private static final int RUSH_HOUR = 22;

    private Backup backup;
    private CronJob cronJob;
    private Action expected;

    private CronJobStateRepository cronJobStateRepository;
    private TimeService timeServiceStub;

    public CronJobDailyBackupOrCheckChangesTest(final String name, final String lastBackupDate, final String currentDate, final Action expected) throws ServiceException {
        this.timeServiceStub = new TimeServiceStub(parse(currentDate));

        final CronJobState cronJobState = new CronJobState(parse(lastBackupDate), 0, 0, 0, 0);
        this.cronJobStateRepository = new CronJobStateRepositoryStub(cronJobState);

        final Sender sender = mock(Sender.class);

        final Loader loader = mock(Loader.class);
        when(loader.load()).thenReturn(CONTENT);

        this.backup = mock(Backup.class);

        final Transactions transactions = mock(Transactions.class);
        final TransactionStub transactionStub = new TransactionStub();
        when(transactions.beginOne()).thenReturn(transactionStub);

        final CronJobConfiguration configuration = new CronJobConfiguration(1, RUSH_HOUR, new Recipients(EMAIL, Collections.singletonList("admin@a.com")));
        this.cronJob = new CronJob(configuration, loader, sender, this.backup, this.cronJobStateRepository, this.timeServiceStub, transactions);

        this.expected = expected;
    }

    private static long parse(String date) {
        return DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss").parseDateTime(date).getMillis();
    }

    @Test
    public void test() {
        final CronJobState origin = this.cronJobStateRepository.load();
        final long originLastDailyBackupTimestamp = origin.getLastDailyBackupTimestamp();

        this.cronJob.execute();

        final CronJobState updated = this.cronJobStateRepository.load();
        final long updatedLastDailyBackupTimestamp = updated.getLastDailyBackupTimestamp();

        switch (this.expected) {

            case DAILY_BACKUP:
                assertEquals(this.timeServiceStub.currentTimestamp(), updatedLastDailyBackupTimestamp);

                verify(this.backup).dailyBackup(CONTENT);
                verifyNoMoreInteractions(this.backup);

                break;
            case CHECK_CHANGES:
                assertEquals(originLastDailyBackupTimestamp, updatedLastDailyBackupTimestamp);

                verify(this.backup).checkChanges(CONTENT);
                verifyNoMoreInteractions(this.backup);

                break;
        }
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[][]{
                        {
                                "if backup has performed in last 24 hours then check changes",
                                "29/12/2017 22:00:05",
                                "29/12/2017 22:10:05",
                                Action.CHECK_CHANGES
                        },
                        {
                                "if backup has performed in last 24 hours then check changes",
                                "29/12/2017 22:00:05",
                                "30/12/2017 00:10:05",
                                Action.CHECK_CHANGES
                        },
                        {
                                "if backup has performed in last 24 hours then check changes",
                                "29/12/2017 22:00:05",
                                "30/12/2017 21:59:59",
                                Action.CHECK_CHANGES
                        },
                        {
                                "if backup has not performed in last 24 hours then backup",
                                "28/12/2017 22:00:05",
                                "29/12/2017 22:00:05",
                                Action.DAILY_BACKUP
                        },
                        {
                                "when last backup has performed later than rush hour then new backup will be performed in rush time",
                                "28/12/2017 03:00:05",
                                "28/12/2017 22:00:05",
                                Action.DAILY_BACKUP
                        },
                        {
                                "when last backup has performed later than rush hour then new backup will be performed in rush time",
                                "28/12/2017 03:00:05",
                                "28/12/2017 21:59:59",
                                Action.CHECK_CHANGES
                        },
                        {
                                "one hour bug",
                                "28/12/2017 03:00:05",
                                "28/12/2017 03:05:05",
                                Action.CHECK_CHANGES
                        }
                }
        );
    }

}
