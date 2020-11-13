package mp.jprime.dataaccess.validators;

import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPUpdate;

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
   * Перед удалением
   *
   * @param query JPDelete
   */
  void beforeDelete(JPDelete query);
}
