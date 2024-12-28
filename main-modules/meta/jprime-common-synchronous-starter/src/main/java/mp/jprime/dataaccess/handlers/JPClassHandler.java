package mp.jprime.dataaccess.handlers;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPUpdate;

/**
 * Интерфейс хендлера
 */
public interface JPClassHandler {
  /**
   * Перед созданием производим поиск на совпадение
   *
   * @param query JPCreate
   * @return Идентификатор найденного объекта
   */
  JPId find(JPCreate query);

  /**
   * Перед созданием до открытия транзакции
   *
   * @param query JPCreate
   */
  void beforeCommitCreate(JPCreate query);

  /**
   * Перед обновлением до открытия транзакции
   *
   * @param query JPUpdate
   */
  void beforeCommitUpdate(JPUpdate query);

  /**
   * Перед удалением до открытия транзакции
   *
   * @param query JPDelete
   */
  void beforeCommitDelete(JPDelete query);

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
   * Перед удалением
   *
   * @param query JPDelete
   */
  void beforeDelete(JPDelete query);

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
   * После удаления
   *
   * @param query JPDelete
   */
  void afterDelete(JPDelete query);

  /**
   * После закрытия транзакции на создание
   *
   * @param objectId Идентификатор объекта
   * @param query    JPCreate
   */
  void afterCommitCreate(Comparable objectId, JPCreate query);

  /**
   * После закрытия транзакции на обновление
   *
   * @param query JPUpdate
   */
  void afterCommitUpdate(JPUpdate query);

  /**
   * После закрытия транзакции на удаление
   *
   * @param query JPDelete
   */
  void afterCommitDelete(JPDelete query);
}
