package mp.jprime.dataaccess.validators;

import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPUpdate;

/**
 * Валидатор данных
 */
public interface JPClassValidator {
  enum Type {
    CREATE, UPDATE, DELETE
  }

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
