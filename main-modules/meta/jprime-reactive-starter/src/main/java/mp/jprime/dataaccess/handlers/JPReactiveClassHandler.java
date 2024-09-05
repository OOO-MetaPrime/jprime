package mp.jprime.dataaccess.handlers;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPUpdate;
import reactor.core.publisher.Mono;

/**
 * Интерфейс хендлера
 */
public interface JPReactiveClassHandler {
  /**
   * Перед созданием производим поиск на совпадение
   *
   * @param query JPCreate
   * @return Идентификатор найденного объекта
   */
  Mono<JPId> find(JPCreate query);

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
   * После создания
   *
   * @param newObjectId Идентификатор созданного объекта
   * @param query       JPCreate
   */
  Mono<Void> afterCreate(Comparable newObjectId, JPCreate query);

  /**
   * После обновления
   *
   * @param query JPUpdate
   */
  Mono<Void> afterUpdate(JPUpdate query);

  /**
   * Перед удалением
   *
   * @param query JPDelete
   */
  Mono<Void> beforeDelete(JPDelete query);

  /**
   * После удаления
   *
   * @param query JPDelete
   */
  Mono<Void> afterDelete(JPDelete query);
}
