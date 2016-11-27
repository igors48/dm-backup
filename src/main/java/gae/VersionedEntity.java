package gae;

/**
 * Created by igor on 27.11.2016.
 */
public class VersionedEntity<T> {

    private final int version;
    private final T payload;

    public VersionedEntity(final int version, final T payload) {
        this.version = version;
        this.payload = payload;
    }

    public int getVersion() {
        return this.version;
    }

    public T getPayload() {
        return this.payload;
    }

}
