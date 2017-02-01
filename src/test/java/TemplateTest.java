import org.junit.Test;
import service.Template;

/**
 * Created by igor on 01.02.2017.
 */
public class TemplateTest {

    @Test
    public void smoke() throws Exception {
        final Template template = new Template();
        String result = template.formatContent();

        System.out.println(result);
    }
}
