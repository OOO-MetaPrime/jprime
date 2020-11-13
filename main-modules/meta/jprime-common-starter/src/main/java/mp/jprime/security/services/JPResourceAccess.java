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
   * Возвращает ограничение данных
   *
   * @return ограничение данных
   */
  Filter getFilter();
}
