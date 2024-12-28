package mp.jprime.dataaccess.handlers;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPUpdate;
import reactor.core.publisher.Mono;

/**
 * Логика вызова CRUD-хендлеров
 */
public interface JPReactiveClassHandlerStorage {
  /**
   * Перед созданием производим поиск на совпадение
   *
   * @param query JPCreate
   * @return Идентификатор найденного объекта
   */
  Mono<JPId> find(JPCreate query);

  /**
   * Перед созданием до открытия транзакции
   *
   * @param query JPCreate
   */
  Mono<Void> beforeCommitCreate(JPCreate query);

  /**
   * Перед обновлением до открытия транзакции
   *
   * @param query JPUpdate
   */
  Mono<Void> beforeCommitUpdate(JPUpdate query);

  /**
   * Перед удалением до открытия транзакции
   *
   * @param query JPDelete
   */
  Mono<Void> beforeCommitDelete(JPDelete query);

  /**
   * Перед созданием
   *
   * @param query JPCreate
   */
  Mono<Void> beforeCreate(JPCreate query);

  /**
   * Перед обновлением
   *
   * @param query JPUpdate
   */
  Mono<Void> beforeUpdate(JPUpdate query);

  /**
   * Перед удалением
   *
   * @param query JPDelete
   */
  Mono<Void> beforeDelete(JPDelete query);

  /**
   * После создания
   *
   * @param objectId Идентификатор объекта
   * @param query    JPCreate
   */
  Mono<Void> afterCreate(Comparable objectId, JPCreate query);

  /**
   * После обновления
   *
   * @param query JPUpdate
   */
  Mono<Void> afterUpdate(JPUpdate query);

  /**
   * После удалениея
   *
   * @param query JPDelete
   */
  Mono<Void> afterDelete(JPDelete query);

  /**
   * После закрытия транзакции на создание
   *
   * @param objectId Идентификатор объекта
   * @param query    JPCreate
   */
  Mono<Void> afterCommitCreate(Comparable objectId, JPCreate query);

  /**
   * После закрытия транзакции на обновление
   *
   * @param query JPUpdate
   */
  Mono<Void> afterCommitUpdate(JPUpdate query);

  /**
   * После закрытия транзакции на удаление
   *
   * @param query JPDelete
   */
  Mono<Void> afterCommitDelete(JPDelete query);
}
