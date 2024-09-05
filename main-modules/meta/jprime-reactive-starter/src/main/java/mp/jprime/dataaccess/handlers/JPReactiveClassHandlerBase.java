package mp.jprime.dataaccess.handlers;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPUpdate;
import reactor.core.publisher.Mono;

/**
 * Базовая реализация хендлера
 */
public abstract class JPReactiveClassHandlerBase implements JPReactiveClassHandler {
  @Override
  public Mono<JPId> find(JPCreate query) {
    return Mono.empty();
  }

  @Override
  public Mono<Void> beforeCreate(JPCreate query) {
    return Mono.empty();
  }

  @Override
  public Mono<Void> beforeUpdate(JPUpdate query) {
    return Mono.empty();
  }

  @Override
  public Mono<Void> beforeDelete(JPDelete query) {
    return Mono.empty();
  }

  @Override
  public Mono<Void> afterCreate(Comparable newObjectId, JPCreate query) {
    return Mono.empty();
  }

  @Override
  public Mono<Void> afterUpdate(JPUpdate query) {
    return Mono.empty();
  }

  @Override
  public Mono<Void> afterDelete(JPDelete query) {
    return Mono.empty();
  }
}
