import org.junit.Before;
import org.junit.Test;
import service.Action;
import service.Content;
import service.Snapshot;
import service.Type;
import util.account.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by igor on 16.04.2017.
 */
public class ChangesDetectorContentSaveTest extends ChangesDetectorTestBase {

    private static final String LATEST_FILE = "latestFile";

    private List<Account> preLatestAccounts;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        this.preLatestAccounts = new ArrayList<>();
        preLatestAccounts.add(TestData.createAccount("a", "1.0"));
        preLatestAccounts.add(TestData.createAccount("b", "2.0"));
        final String preLatestFile = "preLatestFile";
        final Content preLatestContent = new Content(preLatestAccounts, preLatestFile);
        final Snapshot preLatestSnapshot = new Snapshot(UUID.randomUUID(), Type.CHANGE, 1, preLatestContent);

        when(this.snapshotRepository.loadPreLatestSnapshot()).thenReturn(preLatestSnapshot);

        final List<Account> latestAccounts = new ArrayList<>();
        latestAccounts.add(TestData.createAccount("a", "4.0"));
        latestAccounts.add(TestData.createAccount("b", "2.0"));
        final Content latestContent = new Content(latestAccounts, LATEST_FILE);
        final Snapshot latestSnapshot = new Snapshot(UUID.randomUUID(), Type.CHANGE, 1, latestContent);

        when(this.snapshotRepository.loadLatestSnapshot()).thenReturn(latestSnapshot);
    }

    @Test
    public void whenWaitPeriodIsExpiredThenActionIsSend() throws Exception {
        final Action action = this.changesDetector.getActionForContent(LATEST_FILE);

        assertEquals(Action.Type.SEND, action.type);
    }

    @Test
    public void whenWaitPeriodIsExpiredThenPreLastestAccountsUsedForComparison() throws Exception {
        final Action action = this.changesDetector.getActionForContent(LATEST_FILE);

        assertEquals(action.accounts, this.preLatestAccounts);
    }

    @Test
    public void whenWaitPeriodIsExpiredAndNoPrelatestSnapshotThenEmptyAccountsUsedForComparison() throws Exception {
        when(this.snapshotRepository.loadLatestSnapshot()).thenReturn(null);

        final Action action = this.changesDetector.getActionForContent(LATEST_FILE);

        assertEquals(action.accounts, new ArrayList<Account>());
    }

}
