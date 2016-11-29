package gae;

import service.TimeService;

/**
 * Created by igor on 29.11.2016.
 */
public enum GaeTimeService implements TimeService {

    INSTANCE;

    @Override
    public long currentTimestamp() {
        return System.currentTimeMillis();
    }

}
