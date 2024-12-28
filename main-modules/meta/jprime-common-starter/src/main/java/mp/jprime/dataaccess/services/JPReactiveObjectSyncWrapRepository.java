package mp.jprime.dataaccess.services;

import mp.jprime.dataaccess.JPReactiveObjectRepository;
import mp.jprime.dataaccess.JPSyncObjectRepository;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.params.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * API JPReactiveObjectRepository над JPSyncObjectRepository
 */
public final class JPReactiveObjectSyncWrapRepository implements JPReactiveObjectRepository {
  private final JPSyncObjectRepository repo;

  private JPReactiveObjectSyncWrapRepository(JPSyncObjectRepository repo) {
    this.repo = repo;
  }

  public static JPReactiveObjectRepository of(JPSyncObjectRepository repo) {
    return new JPReactiveObjectSyncWrapRepository(repo);
  }

  @Override
  public Mono<Long> getAsyncTotalCount(JPSelect query) {
    return Mono.fromCallable(() -> repo.getTotalCount(query))
        .subscribeOn(getReactorScheduler());
  }

  @Override
  public Flux<JPObject> getAsyncList(JPSelect query) {
    return Mono.fromCallable(() -> repo.getList(query))
        .flatMapMany(Flux::fromIterable)
        .subscribeOn(getReactorScheduler());
  }

  @Override
  public Mono<JPData> getAsyncAggregate(JPAggregate aggr) {
    return Mono.fromCallable(() -> repo.getAggregate(aggr))
        .subscribeOn(getReactorScheduler());
  }

  @Override
  public Mono<JPId> asyncCreate(JPCreate query) {
    return Mono.fromCallable(() -> repo.create(query))
        .subscribeOn(getReactorScheduler());
  }

  @Override
  public Mono<JPObject> asyncCreateAndGet(JPCreate query) {
    return Mono.fromCallable(() -> repo.createAndGet(query))
        .subscribeOn(getReactorScheduler());
  }

  @Override
  public Mono<JPId> asyncUpdate(JPUpdate query) {
    return Mono.fromCallable(() -> repo.update(query))
        .subscribeOn(getReactorScheduler());
  }

  @Override
  public Mono<JPObject> asyncUpdateAndGet(JPUpdate query) {
    return Mono.fromCallable(() -> repo.updateAndGet(query))
        .subscribeOn(getReactorScheduler());
  }

  @Override
  public Mono<JPId> asyncPatch(JPCreate query) {
    return Mono.fromCallable(() -> repo.patch(query))
        .subscribeOn(getReactorScheduler());
  }

  @Override
  public Mono<JPObject> asyncPatchAndGet(JPCreate query) {
    return Mono.fromCallable(() -> repo.patchAndGet(query))
        .subscribeOn(getReactorScheduler());
  }

  @Override
  public Mono<Long> asyncDelete(JPDelete query) {
    return Mono.fromCallable(() -> repo.delete(query))
        .subscribeOn(getReactorScheduler());
  }
}
