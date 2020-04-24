package mp.jprime.dataaccess.validators;

import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPUpdate;

/**
 * Базовая реализация хендлера
 */
public abstract class JPClassValidatorBase implements JPClassValidator {
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
}
