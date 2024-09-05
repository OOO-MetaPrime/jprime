package mp.jprime.meta;

import mp.jprime.beans.JPPropertyType;
import mp.jprime.common.JPEnum;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.meta.beans.JPStringFormat;

import java.util.Collection;

/**
 * Метаописание свойства
 */
public interface JPProperty {
  /**
   * Кодовое имя свойства
   *
   * @return Кодовое имя свойства
   */
  String getCode();

  /**
   * Возвращает признак обязательности
   *
   * @return Да/Нет
   */
  boolean isMandatory();

  /**
   * Тип свойства
   *
   * @return Тип свойства
   */
  JPPropertyType getType();

  /**
   * Тип строкового поля
   *
   * @return Тип строкового поля
   */
  JPStringFormat getStringFormat();

  /**
   * Маска строкового поля
   *
   * @return Маска строкового поля
   */
  String getStringMask();

  /**
   * Возвращает длину
   *
   * @return Длина
   */
  Integer getLength();

  /**
   * Название свойства
   *
   * @return Название свойства
   */
  String getName();

  /**
   * Уникальный qName свойства
   *
   * @return Уникальный qName свойства
   */
  String getQName();

  /**
   * Код класса, на который ссылается
   *
   * @return Код класса, на который ссылается
   */
  String getRefJpClass();

  /**
   * Код атрибута, на который ссылается
   *
   * @return Код атрибута, на который ссылается
   */
  String getRefJpAttr();

  /**
   * Условие, на объекты класса
   *
   * @return Условие
   */
  Filter getFilter();

  /**
   * Возвращает перечислимые значения
   *
   * @return Перечислимые значения
   */
  Collection<JPEnum> getEnums();

  /**
   * Список вложенных свойств
   *
   * @return список вложенных свойств
   */
  Collection<JPProperty> getJpProps();
}
