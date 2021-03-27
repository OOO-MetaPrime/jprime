package mp.jprime.dataaccess.handlers;

import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPUpdate;
import reactor.core.publisher.Mono;

/**
 * Базовая реализация хендлера
 */
public abstract class JPReactiveClassHandlerBase implements JPReactiveClassHandler {
  /**
   * Перед созданием
   *
   * @param query JPCreate
   */
  @Override
  public Mono<Void> beforeCreate(JPCreate query) {
    return Mono.empty();
  }

  /**
   * Перед обновлением
   *
   * @param query JPUpdate
   */
  @Override
  public Mono<Void> beforeUpdate(JPUpdate query) {
    return Mono.empty();
  }

  /**
   * Перед удалением
   *
   * @param query JPDelete
   */
  @Override
  public Mono<Void> beforeDelete(JPDelete query) {
    return Mono.empty();
  }

  /**
   * После создания
   *
   * @param newObjectId Идентификатор созданного объекта
   * @param query       JPCreate
   */
  @Override
  public Mono<Void> afterCreate(Comparable newObjectId, JPCreate query) {
    return Mono.empty();
  }

  /**
   * После обновления
   *
   * @param query JPUpdate
   */
  @Override
  public Mono<Void> afterUpdate(JPUpdate query) {
    return Mono.empty();
  }

  /**
   * После удаления
   *
   * @param query JPDelete
   */
  @Override
  public Mono<Void> afterDelete(JPDelete query) {
    return Mono.empty();
  }
}
