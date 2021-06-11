package mp.jprime.dataaccess.templatevalues;

import mp.jprime.security.AuthInfo;

/**
 * Сервис получения шаблонных значения
 */
public interface JPTemplateValueService {
  /**
   * Форматируем шаблонное значение
   *
   * @param value    Значение
   * @param authInfo AuthInfo
   * @return Значение поля
   */
  Object getValue(Object value, AuthInfo authInfo);
}
