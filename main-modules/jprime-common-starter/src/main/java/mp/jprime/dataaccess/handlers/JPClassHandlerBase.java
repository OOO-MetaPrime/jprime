package mp.jprime.dataaccess.handlers;

import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPUpdate;

/**
 * Базовая реализация хендлера
 */
public abstract class JPClassHandlerBase implements JPClassHandler {
  /**
   * Перед созданием
   *
   * @param query JPCreate
   */
  @Override
  public void beforeCreate(JPCreate query) {

  }

  /**
   * Перед обновлением
   *
   * @param query JPUpdate
   */
  @Override
  public void beforeUpdate(JPUpdate query) {

  }

  /**
   * Перед удалением
   *
   * @param query JPDelete
   */
  @Override
  public void beforeDelete(JPDelete query) {

  }

  /**
   * После создания
   *
   * @param newObjectId Идентификатор созданного объекта
   * @param query       JPCreate
   */
  @Override
  public void afterCreate(Comparable newObjectId, JPCreate query) {

  }

  /**
   * После обновления
   *
   * @param query JPUpdate
   */
  @Override
  public void afterUpdate(JPUpdate query) {

  }

  /**
   * После удаления
   *
   * @param query JPDelete
   */
  @Override
  public void afterDelete(JPDelete query) {

  }
}
