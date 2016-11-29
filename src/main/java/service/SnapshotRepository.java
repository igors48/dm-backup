package service;

import java.util.List;

/**
 * Created by igor on 25.11.2016.
 */
public interface SnapshotRepository {

    String loadLatestSnapshot();

    List<Snapshot> loadAll();

    void storeAll(List<Snapshot> snapshots);

    void clear();

}
