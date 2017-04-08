import org.junit.Test;
import util.account.AccountsParser;
import util.account.ComparisonResult;
import util.account.ParsedAccount;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by igor on 01.04.2017.
 */
public class AccountsComparatorTest {

    private static final ParsedAccount FIRST_BEFORE = new ParsedAccount("first", 2.3f);
    private static final ParsedAccount SECOND_BEFORE = new ParsedAccount("second", 4.5f);
    private static final ParsedAccount THIRD_BEFORE = new ParsedAccount("third", 6.7f);

    private static final ParsedAccount FIRST_AFTER = new ParsedAccount("first", 3.4f);
    private static final ParsedAccount SECOND_AFTER = new ParsedAccount("second", 4.5f);
    private static final ParsedAccount THIRD_AFTER = new ParsedAccount("third", 7.8f);

    @Test
    public void whenBothListsAreIdenticalThenItemsMergedAndSortedAlpabetically() throws Exception {
        final List<ParsedAccount> before = Arrays.asList(FIRST_BEFORE, SECOND_BEFORE, THIRD_BEFORE);
        final List<ParsedAccount> after = Arrays.asList(FIRST_AFTER, SECOND_AFTER, THIRD_AFTER);

        final List<ComparisonResult> result = AccountsParser.compare(before, after);

        assertEquals(3, result.size());

        assertEquals(new ComparisonResult(FIRST_BEFORE.title, FIRST_BEFORE.balance, FIRST_AFTER.balance), result.get(0));
        assertEquals(new ComparisonResult(SECOND_BEFORE.title, SECOND_BEFORE.balance, SECOND_AFTER.balance), result.get(1));
        assertEquals(new ComparisonResult(THIRD_BEFORE.title, THIRD_BEFORE.balance, THIRD_AFTER.balance), result.get(2));
    }

    @Test
    public void whenItemDeletedThenItIsAddedToResultWithAfterIsNull() throws Exception {
        final List<ParsedAccount> before = Arrays.asList(FIRST_BEFORE, SECOND_BEFORE, THIRD_BEFORE);
        final List<ParsedAccount> after = Arrays.asList(FIRST_AFTER, THIRD_AFTER);

        final List<ComparisonResult> result = AccountsParser.compare(before, after);

        assertEquals(3, result.size());

        assertEquals(new ComparisonResult(FIRST_BEFORE.title, FIRST_BEFORE.balance, FIRST_AFTER.balance), result.get(0));
        assertEquals(new ComparisonResult(SECOND_BEFORE.title, SECOND_BEFORE.balance, null), result.get(1));
        assertEquals(new ComparisonResult(THIRD_BEFORE.title, THIRD_BEFORE.balance, THIRD_AFTER.balance), result.get(2));
    }

    @Test
    public void whenItemAddedThenItIsAddedToResultWithBeforeIsNull() throws Exception {
        final List<ParsedAccount> before = Arrays.asList(FIRST_BEFORE, THIRD_BEFORE);
        final List<ParsedAccount> after = Arrays.asList(FIRST_AFTER, SECOND_AFTER, THIRD_AFTER);

        final List<ComparisonResult> result = AccountsParser.compare(before, after);

        assertEquals(3, result.size());

        assertEquals(new ComparisonResult(FIRST_BEFORE.title, FIRST_BEFORE.balance, FIRST_AFTER.balance), result.get(0));
        assertEquals(new ComparisonResult(SECOND_BEFORE.title, null, SECOND_AFTER.balance), result.get(1));
        assertEquals(new ComparisonResult(THIRD_BEFORE.title, THIRD_BEFORE.balance, THIRD_AFTER.balance), result.get(2));
    }

}
