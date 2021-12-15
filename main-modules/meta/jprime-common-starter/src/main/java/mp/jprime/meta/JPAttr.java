package mp.jprime.meta;

import mp.jprime.meta.beans.JPType;

import java.util.Collection;

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
   * Признак обновляемости значения атрибута
   *
   * @return Да/Нет
   */
  default boolean isUpdatable() {
    JPType type = getType();
    return type != JPType.VIRTUALREFERENCE && type != JPType.NONE;
  }

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
   * Возвращает описание файла
   *
   * @return Описание файла
   */
  JPFile getRefJpFile();

  /**
   * Возвращает описание простой дроби
   *
   * @return Описание простой дроби
   */
  JPSimpleFraction getSimpleFraction();

  /**
   * Возвращает описание денежного типа
   *
   * @return Описание денежного типа
   */
  JPMoney getMoney();

  /**
   * Описание виртуальной ссылки
   *
   * @return Описание виртуальной ссылки
   */
  JPVirtualPath getVirtualReference();

  /**
   * Тип значения атрибута
   *
   * @return Тип значения атрибута
   */
  default JPType getValueType() {
    JPVirtualPath path = getType() == JPType.VIRTUALREFERENCE ? getVirtualReference() : null;
    return path != null && path.getType() != null ? path.getType() : getType();
  }

  /**
   * Схема свойств псевдо-меты
   *
   * @return свойства псевдо-меты
   */
  Collection<JPProperty> getSchemaProps();
}
