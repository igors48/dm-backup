package service;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 14.11.2016.
 */
public class Backup {

    private final Loader loader;
    private final Sender sender;

    public Backup(final Loader loader, final Sender sender) {
        guard(notNull(this.loader = loader));
        guard(notNull(this.sender = sender));
    }

    public void execute() {

        try {
            this.sender.sendContent(this.loader.load());
        } catch (ServiceException exception) {
            sendError(exception);
        }
    }

    private void sendError(ServiceException exception) {

        try {
            this.sender.sendError(exception.error);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

}
