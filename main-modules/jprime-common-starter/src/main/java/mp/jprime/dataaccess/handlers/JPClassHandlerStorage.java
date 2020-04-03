package mp.jprime.dataaccess.handlers;

import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPUpdate;

/**
 * Логика вызова CRUD-хендлеров
 */
public interface JPClassHandlerStorage {
  /**
   * Перед созданием
   *
   * @param query JPCreate
   */
  void beforeCreate(JPCreate query);

  /**
   * Перед обновлением
   *
   * @param query JPUpdate
   */
  void beforeUpdate(JPUpdate query);

  /**
   * После создания
   *
   * @param objectId Идентификатор объекта
   * @param query    JPCreate
   */
  void afterCreate(Comparable objectId, JPCreate query);

  /**
   * После обновления
   *
   * @param query JPUpdate
   */
  void afterUpdate(JPUpdate query);

  /**
   * Перед удалением
   *
   * @param query JPDelete
   */
  void beforeDelete(JPDelete query);

  /**
   * После удалениея
   *
   * @param query JPDelete
   */
  void afterDelete(JPDelete query);
}
