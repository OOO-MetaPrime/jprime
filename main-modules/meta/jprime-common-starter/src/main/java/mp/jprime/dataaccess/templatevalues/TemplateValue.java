package mp.jprime.dataaccess.templatevalues;

import mp.jprime.security.AuthInfo;

/**
 * Шаблонные значения
 */
public interface TemplateValue {
  /**
   * Возвращает шаблон
   * @return Шаблон
   */
  String getTemplate();

  /**
   * Форматируем шаблонное значение
   *
   * @param value    Значение
   * @param authInfo AuthInfo
   * @return Значение поля
   */
  Object getValue(Object value, AuthInfo authInfo);
}
