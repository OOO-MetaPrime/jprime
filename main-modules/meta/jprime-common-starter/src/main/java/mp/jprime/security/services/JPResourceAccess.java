package mp.jprime.security.services;

import mp.jprime.dataaccess.params.query.Filter;

/**
 * Доступ к ресурсу
 */
public interface JPResourceAccess {
  /**
   * Доступ
   *
   * @return Да/Нет
   */
  boolean isAccess();

  /**
   * Отсутствие доступа
   *
   * @return Да/Нет
   */
  default boolean isNotAccess() {
    return !isAccess();
  }

  /**
   * Возвращает ограничение данных
   *
   * @return ограничение данных
   */
  Filter getFilter();
}
