package service;

/**
 * Created by igor on 25.11.2016.
 */
public interface ChangesRepository {

    void store(ChangesTimestamp changesTimestamp);

    ChangesTimestamp load();

}
