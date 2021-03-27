package mp.jprime.meta;

import mp.jprime.beans.PropertyType;

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
   * Возвращает признак множественности
   *
   * @return Да/Нет
   */
  boolean isMultiple();

  /**
   * Тип свойства
   *
   * @return Тип свойства
   */
  PropertyType getType();

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
   * Короткое название свойства
   *
   * @return Короткое название свойства
   */
  String getShortName();

  /**
   * Описание свойства
   *
   * @return Описание свойства
   */
  String getDescription();

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
  String getRefJpClassCode();

  /**
   * Код атрибута, на который ссылается
   *
   * @return Код атрибута, на который ссылается
   */
  String getRefJpAttrCode();

  /**
   * Список вложенных свойств
   *
   * @return список вложенных свойств
   */
  Collection<JPProperty> getSchemaProps();
}
