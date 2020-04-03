package mp.jprime.meta;

import mp.jprime.meta.beans.JPType;

/**
 * метаописание атрибута
 */
public interface JPAttr {
  /**
   * Код класса
   *
   * @return Код класса
   */
  String getJpClassCode();

  /**
   * Идентификатор/гуид атрибута
   *
   * @return Идентификатор/гуид атрибута
   */
  String getGuid();

  /**
   * Кодовое имя атрибута
   *
   * @return Кодовое имя атрибута
   */
  String getCode();

  /**
   * Возвращает признак идентификатора
   *
   * @return Да/Нет
   */
  boolean isIdentifier();

  /**
   * Возвращает признак обязательности
   *
   * @return Да/Нет
   */
  boolean isMandatory();

  /**
   * Тип атрибута
   *
   * @return Тип атрибута
   */
  JPType getType();

  /**
   * Возвращает длину
   *
   * @return Длина
   */
  Integer getLength();

  /**
   * Название класса
   *
   * @return Название класса
   */
  String getName();

  /**
   * Короткое название класса
   *
   * @return Короткое название класса
   */
  String getShortName();

  /**
   * Описание атрибута
   *
   * @return Описание атрибута
   */
  String getDescription();

  /**
   * Уникальный qName атрибута
   *
   * @return Уникальный qName атрибута
   */
  String getQName();

  /**
   * Кодовое имя пакета/группировки метаописания атрибута
   *
   * @return Кодовое имя пакета/группировки метаописания атрибута
   */
  String getJpPackage();

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
   * Путь виртуальной ссылки
   *
   * @return Путь виртуальной ссылки
   */
  String getVirtualReference();

  /**
   * Тип виртуальной ссылки
   *
   * @return Тип виртуальной ссылки
   */
  JPType getVirtualType();
}
