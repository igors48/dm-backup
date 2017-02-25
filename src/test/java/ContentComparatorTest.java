import org.junit.Test;
import service.ChangesDetector;

import java.io.File;

import static java.nio.file.Files.readAllBytes;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by igor on 21.02.2017.
 */
public class ContentComparatorTest {

    @Test
    public void whenOnlyFirstLineIsChangedThenContentsAreEqual() throws Exception {
        final String dump01 = loadDump("dump01.txt");
        final String dump02 = loadDump("dump02.txt");

        assertFalse(ChangesDetector.isContentChanged(dump01, dump02));
    }

    @Test
    public void whenNotOnlyFirstLineIsChangedThenContentsAreNotEqual() throws Exception {
        final String dump01 = loadDump("dump01.txt");
        final String dump03 = loadDump("dump03.txt");

        assertTrue(ChangesDetector.isContentChanged(dump01, dump03));
    }

    private String loadDump(final String fileName) throws Exception {
        final ClassLoader classLoader = this.getClass().getClassLoader();
        final File file = new File(classLoader.getResource(fileName).getFile());

        return new String(readAllBytes(file.toPath()));
    }

}
