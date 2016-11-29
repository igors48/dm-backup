package gae;

import service.TimeService;

/**
 * Created by igor on 29.11.2016.
 */
public class GaeTimeService implements TimeService {

    @Override
    public long currentTimestamp() {
        return System.currentTimeMillis();
    }

}
