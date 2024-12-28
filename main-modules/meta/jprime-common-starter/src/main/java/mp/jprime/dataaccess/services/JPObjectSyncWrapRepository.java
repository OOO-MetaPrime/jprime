package mp.jprime.dataaccess.services;

import mp.jprime.dataaccess.JPObjectRepository;
import mp.jprime.dataaccess.JPSyncObjectRepository;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.params.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * API JPObjectRepository над JPSyncObjectRepository
 */
public final class JPObjectSyncWrapRepository implements JPObjectRepository {
  private final JPSyncObjectRepository repo;

  private JPObjectSyncWrapRepository(JPSyncObjectRepository repo) {
    this.repo = repo;
  }

  public static JPObjectRepository of(JPSyncObjectRepository repo) {
    return new JPObjectSyncWrapRepository(repo);
  }

  @Override
  public Long getTotalCount(JPSelect query) {
    return repo.getTotalCount(query);
  }

  @Override
  public Collection<JPObject> getList(JPSelect query) {
    return repo.getList(query);
  }

  @Override
  public JPData getAggregate(JPAggregate aggr) {
    return repo.getAggregate(aggr);
  }

  @Override
  public JPId create(JPCreate query) {
    return repo.create(query);
  }

  @Override
  public JPObject createAndGet(JPCreate query) {
    return repo.createAndGet(query);
  }

  @Override
  public JPId update(JPUpdate query) {
    return repo.update(query);
  }

  @Override
  public Long update(JPConditionalUpdate query) {
    return repo.update(query);
  }

  @Override
  public JPObject updateAndGet(JPUpdate query) {
    return repo.updateAndGet(query);
  }

  @Override
  public JPId patch(JPCreate query) {
    return repo.patch(query);
  }

  @Override
  public JPObject patchAndGet(JPCreate query) {
    return repo.patchAndGet(query);
  }

  @Override
  public Long delete(JPDelete query) {
    return repo.delete(query);
  }

  @Override
  public Long delete(JPConditionalDelete query) {
    return repo.delete(query);
  }

  @Override
  public JPObject getObject(JPSelect query) {
    return repo.getObject(query);
  }

  @Override
  public JPObject getObjectAndLock(JPSelect query) {
    return repo.getObjectAndLock(query);
  }

  @Override
  public JPObject getObjectAndLock(JPSelect query, boolean skipLocked) {
    return repo.getObjectAndLock(query, skipLocked);
  }

  @Override
  public Optional<JPObject> getOptionalObject(JPSelect query) {
    return repo.getOptionalObject(query);
  }

  @Override
  public Collection<JPObject> getListAndLock(JPSelect query) {
    return repo.getListAndLock(query);
  }

  @Override
  public Collection<JPObject> getListAndLock(JPSelect query, boolean skipLocked) {
    return repo.getListAndLock(query, skipLocked);
  }

  @Override
  public void batch(JPBatchCreate query) {
    repo.batch(query);
  }

  @Override
  public <T> List<T> batchWithKeys(JPBatchCreate query) {
    return repo.batchWithKeys(query);
  }

  @Override
  public void batch(JPBatchUpdate query) {
    repo.batch(query);
  }
}
