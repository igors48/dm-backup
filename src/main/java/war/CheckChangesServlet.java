package war;

import gae.Dependencies;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheckChangesServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CheckChangesServlet.class.getName());

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        try {
            Dependencies.GET.backupService().checkChanges();
        } catch (Throwable throwable) {
            LOGGER.log(Level.SEVERE, "Unexpected error", throwable);
        }

    }

}
