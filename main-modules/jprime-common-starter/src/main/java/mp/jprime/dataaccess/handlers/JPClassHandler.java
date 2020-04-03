package mp.jprime.dataaccess.handlers;

import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPUpdate;

/**
 * Интерфейс хендлера
 */
public interface JPClassHandler {
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
   * @param newObjectId Идентификатор созданного объекта
   * @param query       JPCreate
   */
  void afterCreate(Comparable newObjectId, JPCreate query);

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
   * После удаления
   *
   * @param query JPDelete
   */
  void afterDelete(JPDelete query);
}
