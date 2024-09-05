package mp.jprime.dataaccess.validators;

import mp.jprime.dataaccess.params.*;

/**
 * Валидатор данных
 */
public interface JPClassValidator {
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
   * Перед обновлением
   *
   * @param query JPUpdate
   */
  void beforeUpdate(JPConditionalUpdate query);

  /**
   * Перед удалением
   *
   * @param query JPDelete
   */
  void beforeDelete(JPDelete query);

  /**
   * Перед удалением
   *
   * @param query JPDelete
   */
  void beforeDelete(JPConditionalDelete query);
}
