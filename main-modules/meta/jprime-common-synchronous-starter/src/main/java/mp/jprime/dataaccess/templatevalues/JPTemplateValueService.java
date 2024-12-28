package mp.jprime.dataaccess.templatevalues;

import mp.jprime.security.AuthInfo;

/**
 * Сервис получения шаблонных значения
 */
public interface JPTemplateValueService {
  /**
   * Форматируем шаблонное значение
   *
   * @param value Значение
   * @param auth  AuthInfo
   * @return Значение поля
   */
  Object getValue(Object value, AuthInfo auth);

  /**
   * Форматируем шаблонное значение
   *
   * @param templateValue JPTemplateValue
   * @param value         Значение
   * @param auth          AuthInfo
   * @return Значение поля
   */
  Object getValue(JPTemplateValue templateValue, Object value, AuthInfo auth);

  /**
   * Проверяем шаблонное значение
   *
   * @param value Значение
   * @return JPTemplateValue
   */
  JPTemplateValue getTemplate(Object value);
}
