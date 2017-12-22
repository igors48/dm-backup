import org.junit.Before;
import service.*;
import service.configuration.Recipients;
import service.error.ServiceException;
import util.account.Account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by igor on 17.02.2017.
 */
public class BackupTestBase {

    protected static final Content CONTENT = new Content(new ArrayList<Account>(), "content");

    protected static final List<Account> LATEST_ACCOUNTS = TestData.createAccounts();
    protected static final Content LATEST_CONTENT = new Content(LATEST_ACCOUNTS, "latest_content");
    protected static final Snapshot LATEST_SNAPSHOT = new Snapshot(UUID.randomUUID(), Type.DAILY, 48, LATEST_CONTENT);
    protected static final Snapshot CURRENT_SNAPSHOT = new Snapshot(UUID.randomUUID(), Type.DAILY, 49, LATEST_CONTENT);

    protected static final String A_B_COM = "a@b.com";
    protected static final String B_C_COM = "b@c.com";
    protected static final String C_D_COM = "c@d.com";

    protected Sender sender;
    protected ChangesDetector changesDetector;
    protected Backup backup;
    protected ServiceException serviceException;
    protected SnapshotStore changesSnapshotStore;
    protected SnapshotStore dailySnapshotStore;
    protected TransactionStub transactionStub;
    protected TimeServiceStub timeServiceStub;
    protected Recipients recipients;
    protected Transactions transactions;

    @Before
    public void setUp() {
        this.sender = mock(Sender.class);
        this.changesDetector = mock(ChangesDetector.class);
        this.changesSnapshotStore = mock(SnapshotStore.class);
        this.dailySnapshotStore = mock(SnapshotStore.class);
        this.transactions = mock(Transactions.class);
        this.transactionStub = new TransactionStub();
        this.timeServiceStub = new TimeServiceStub();

        this.serviceException = mock(ServiceException.class);

        this.recipients = new Recipients(A_B_COM, Arrays.asList(B_C_COM, C_D_COM));

        when(this.transactions.beginOne()).thenReturn(this.transactionStub);
        when(this.changesDetector.getActionForContent(CONTENT.file)).thenReturn(Action.NO_ACTION);
        when(this.dailySnapshotStore.loadLatestSnapshot()).thenReturn(LATEST_SNAPSHOT);
        when(this.dailySnapshotStore.store((Content) any())).thenReturn(CURRENT_SNAPSHOT);

        this.backup = new Backup(this.sender, this.changesDetector, this.recipients, this.changesSnapshotStore, this.dailySnapshotStore, this.timeServiceStub, transactions);
    }
}
