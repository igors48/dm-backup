package gae;

import service.*;

/**
 * Created by igor on 20.11.2016.
 */
public enum Services {

    INSTANCE;

    public Backup createBackupService() throws ServiceException {
        final Configuration configuration = Configuration.fromSystemProperties();

        final Loader loader = new Loader(configuration.getAccessParameters());
        final Sender sender = new Sender();

        return new Backup(loader, sender, configuration.getRecipients());
    }

}
