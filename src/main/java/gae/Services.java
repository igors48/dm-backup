package gae;

import service.Backup;
import service.Loader;
import service.Sender;
import service.configuration.Configuration;
import service.error.ServiceException;

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
