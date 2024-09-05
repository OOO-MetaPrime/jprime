package mp.jprime.dataaccess.validators;

import mp.jprime.dataaccess.params.*;

/**
 * Логика вызова валидаторов
 */
public interface JPClassValidatorService {
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
