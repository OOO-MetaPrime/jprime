package mp.jprime.dataaccess.checkers;

import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.lang.JPMap;
import mp.jprime.security.AuthInfo;

import java.util.Collection;

/**
 * Сервис проверки данных указанному условию
 */
public interface JPDataCheckService {
  /**
   * Возвращает количество объектов, удовлетворяющих выборке
   *
   * @param select  JPSelect
   * @param objects Полный список объектов
   * @return Количество в выборке
   */
  Long getTotalCount(JPSelect select, Collection<JPObject> objects);

  /**
   * Фильтрует переданный список объектов по условию JPSelect
   *
   * @param select  JPSelect
   * @param objects Полный список объектов
   * @return Результирующий список
   */
  Collection<JPObject> getList(JPSelect select, Collection<JPObject> objects);

  /**
   * Проверяем условие по переданным данным
   *
   * @param filter                  Условие
   * @param data                    Данные
   * @param auth                    AuthInfo
   * @param notContainsDefaultValue Результат, в случае отсутствия ключа в data
   * @return Да/Нет
   */
  boolean check(Filter filter, JPMap data, AuthInfo auth, boolean notContainsDefaultValue);

  /**
   * Проверяем условие по переданным данным
   *
   * @param filter                  Условие
   * @param data                    Данные
   * @param notContainsDefaultValue Результат, в случае отсутствия ключа в data
   * @return Да/Нет
   */
  default boolean check(Filter filter, JPMap data, boolean notContainsDefaultValue) {
    return check(filter, data, null, notContainsDefaultValue);
  }

  /**
   * Проверяем условие по переданным данным
   *
   * @param filter Условие
   * @param data   Данные
   * @return Да/Нет
   */
  default boolean check(Filter filter, JPMap data) {
    return check(filter, data, null, false);
  }
}
