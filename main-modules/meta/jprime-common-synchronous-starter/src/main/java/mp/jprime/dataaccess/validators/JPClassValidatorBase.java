package mp.jprime.dataaccess.validators;

import mp.jprime.dataaccess.params.*;

/**
 * Базовая реализация валидатора
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
   * Перед обновлением
   *
   * @param query JPUpdate
   */
  @Override
  public void beforeUpdate(JPConditionalUpdate query) {

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
   * Перед удалением
   *
   * @param query JPDelete
   */
  @Override
  public void beforeDelete(JPConditionalDelete query) {

  }
}
