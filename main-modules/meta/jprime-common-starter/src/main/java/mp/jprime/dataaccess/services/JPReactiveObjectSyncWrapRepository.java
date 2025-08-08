package mp.jprime.dataaccess.services;

import mp.jprime.dataaccess.JPReactiveObjectRepository;
import mp.jprime.dataaccess.JPSyncObjectRepository;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.params.*;
import mp.jprime.reactor.core.publisher.JPMono;
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
    return JPMono.fromCallable(() -> repo.getTotalCount(query));
  }

  @Override
  public Flux<JPObject> getAsyncList(JPSelect query) {
    return JPMono.fromCallable(() -> repo.getList(query))
        .flatMapMany(Flux::fromIterable);
  }

  @Override
  public Mono<JPData> getAsyncAggregate(JPAggregate aggr) {
    return JPMono.fromCallable(() -> repo.getAggregate(aggr));
  }

  @Override
  public Mono<JPId> asyncCreate(JPCreate query) {
    return JPMono.fromCallable(() -> repo.create(query));
  }

  @Override
  public Mono<JPObject> asyncCreateAndGet(JPCreate query) {
    return JPMono.fromCallable(() -> repo.createAndGet(query));
  }

  @Override
  public Mono<JPId> asyncUpdate(JPUpdate query) {
    return JPMono.fromCallable(() -> repo.update(query));
  }

  @Override
  public Mono<JPObject> asyncUpdateAndGet(JPUpdate query) {
    return JPMono.fromCallable(() -> repo.updateAndGet(query));
  }

  @Override
  public Mono<JPId> asyncPatch(JPCreate query) {
    return JPMono.fromCallable(() -> repo.patch(query));
  }

  @Override
  public Mono<JPObject> asyncPatchAndGet(JPCreate query) {
    return JPMono.fromCallable(() -> repo.patchAndGet(query));
  }

  @Override
  public Mono<Long> asyncDelete(JPDelete query) {
    return JPMono.fromCallable(() -> repo.delete(query));
  }
}
