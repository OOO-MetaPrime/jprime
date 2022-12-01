package mp.jprime.dataaccess.checkers;

import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.security.AuthInfo;

/**
 * Сервис проверки данных указанному условию
 */
public interface JPDataCheckService {
  /**
   * Проверяем условие по переданным данным
   *
   * @param filter                  Условие
   * @param data                    Данные
   * @param auth                    AuthInfo
   * @param notContainsDefaultValue Результат, в случае отсутствия ключа в data
   * @return Да/Нет
   */
  boolean check(Filter filter, JPAttrData data, AuthInfo auth, boolean notContainsDefaultValue);

  /**
   * Проверяем условие по переданным данным
   *
   * @param filter Условие
   * @param data   Данные
   * @return Да/Нет
   */
  default boolean check(Filter filter, JPAttrData data) {
    return check(filter, data, null, false);
  }
}
