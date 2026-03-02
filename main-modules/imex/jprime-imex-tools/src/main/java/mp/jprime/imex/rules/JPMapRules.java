package mp.jprime.imex.rules;

import mp.jprime.meta.beans.JPType;

import java.util.Collection;

/**
 * Настройки правил мапинга для связывания со столбцом
 *
 * @param <T> тип кода в правилах
 */
public interface JPMapRules<T> {
  /**
   * Определяет тип параметра класса
   *
   * @return Параметр класса
   */
  JPType getKeyType();

  /**
   * Приводит тип настроек правил к указанному
   *
   * @return Тип JPMapRules<Integer>
   */
  default JPMapRules<Integer> toIntegerKeyType() {
    return (JPMapRules<Integer>) this;
  }

  /**
   * Приводит тип настроек правил к указанному
   *
   * @return Тип JPMapRules<String>
   */
  default JPMapRules<String> toStringKeyType() {
    return (JPMapRules<String>) this;
  }

  /**
   * Названия/индексы связываемых столбцов
   *
   * @return Названия/индексы столбцов файла
   */
  Collection<T> getColumns();

  /**
   * Настройки по столбцу
   * @param column Имя/индекс столбца
   * @return Настройки столбца
   */
  JPColumnSettings getSettings(T column);

  /**
   * Настройки чтения столбца
   */
  interface JPColumnSettings {
    /**
     * Имена атрибутов
     *
     * @return Имена атрибутов
     */
    Collection<String> getAttrs();

    /**
     * Тип атрибута
     *
     * @return Тип атрибута
     */
    JPType getAttrType(String attrName);

    /**
     * Признак обязательности столбца
     *
     * @return Признак обязательности столбца
     */
    boolean isMandatory();
  }
}
