package service;

/**
 * Created by igor on 25.11.2016.
 */
public interface TimestampRepository {

    void store(Long timestamp);

    Long load();

    void clear();

}
