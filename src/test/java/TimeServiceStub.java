import service.TimeService;

/**
 * Created by igor on 22.02.2017.
 */
public class TimeServiceStub implements TimeService {

    private long timestamp;

    @Override
    public long currentTimestamp() {
        return ++this.timestamp;
    }

}
