package war;

import gae.Services;
import service.Backup;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CronServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CronServlet.class.getName());

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        try {
            final Backup backup = Services.INSTANCE.createBackupService();

            backup.execute();
        } catch (Throwable throwable) {
            LOGGER.log(Level.SEVERE, "Unexpected error", throwable);
        }

    }

}
