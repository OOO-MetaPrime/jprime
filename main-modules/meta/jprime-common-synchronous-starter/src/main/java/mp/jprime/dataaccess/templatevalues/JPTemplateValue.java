package mp.jprime.dataaccess.templatevalues;

import mp.jprime.security.AuthInfo;

/**
 * Шаблонные значения
 */
public interface JPTemplateValue {
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
   * @param value Значение
   * @param auth  AuthInfo
   * @return Значение поля
   */
  Object getValue(Object value, AuthInfo auth);

  /**
   * Признак игнорирования пустого значения
   *
   * @return Да/Нет
   */
  default boolean isEmptyValueIgnore() {
    return false;
  }
}
