package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.lang.JPMap;
import mp.jprime.security.AuthInfo;

/**
 * Проверка значений указанным условиям
 *
 * @param <T> Filter
 */
public interface CheckFilter<T extends Filter> {
  /**
   * Проверяем условие по переданным данным
   *
   * @param filter                  Условие
   * @param data                    Данные
   * @param auth                    AuthInfo
   * @param notContainsDefaultValue Результат, в случае отсутствия ключа в data
   * @return Да/Нет
   */
  boolean check(T filter, JPMap data, AuthInfo auth, boolean notContainsDefaultValue);
}
