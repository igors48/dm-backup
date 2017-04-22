import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import service.Difference;
import service.Template;
import util.account.ComparisonResult;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Created by igor on 21.04.2017.
 */
@RunWith(value = Parameterized.class)
public class DifferenceRenderTest {

    private ComparisonResult current;
    private Difference expected;

    public DifferenceRenderTest(final ComparisonResult current, final Difference expected) {
        this.current = current;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[][]{
                        {new ComparisonResult("title", 2f, 3f, 1f), new Difference("title", "2,00", "3,00", "1,00", Template.GREEN)},
                        {new ComparisonResult("title", 3f, 2f, -1f), new Difference("title", "3,00", "2,00", "-1,00", Template.RED)},
                        {new ComparisonResult("title", 3f, 3f, 0f), new Difference("title", "3,00", "3,00", "0,00", Template.GRAY)},

                        {new ComparisonResult("title", null, null, 0f), new Difference("title", "", "", "0,00", Template.GRAY)},
                        {new ComparisonResult("title", null, 3f, 3f), new Difference("title", "", "3,00", "3,00", Template.GREEN)},
                        {new ComparisonResult("title", 3f, null, -3f), new Difference("title", "3,00", "", "-3,00", Template.RED)}
                });
    }

    @Test
    public void test() throws Exception {
        assertEquals(this.expected, Template.render(this.current));
    }

}
