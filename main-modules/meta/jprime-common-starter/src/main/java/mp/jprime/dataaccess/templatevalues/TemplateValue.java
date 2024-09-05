package mp.jprime.dataaccess.templatevalues;

import mp.jprime.security.AuthInfo;

/**
 * Шаблонные значения
 */
public interface TemplateValue {
  /**
   * Возвращает шаблон
   *
   * @return Шаблон
   */
  String getTemplate();

  /**
   * Возвращает описание шаблона
   *
   * @return описание
   */
  default String getPattern() {
    return "{" + getTemplate() + "}";
  }

  /**
   * Форматируем шаблонное значение
   *
   * @param value    Значение
   * @param authInfo AuthInfo
   * @return Значение поля
   */
  Object getValue(Object value, AuthInfo authInfo);
}
