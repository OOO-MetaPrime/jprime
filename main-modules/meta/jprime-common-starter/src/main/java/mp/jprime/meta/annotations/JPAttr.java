package mp.jprime.meta.annotations;

import mp.jprime.meta.beans.JPType;

/**
 * Аннотация описания метаатрибута
 */
public @interface JPAttr {
  /**
   * Глобальный идентификатор
   *
   * @return Глобальный идентификатор
   */
  String guid() default "";

  /**
   * Кодовое имя атрибута
   *
   * @return Кодовое имя атрибута
   */
  String code();

  /**
   * Полный код атрибута
   *
   * @return Полный код атрибута
   */
  String qName() default "";

  /**
   * Название атрибута
   *
   * @return Название атрибута
   */
  String name();

  /**
   * Короткое название атрибута
   *
   * @return Короткое название атрибута
   */
  String shortName() default "";

  /**
   * Описание атрибута
   *
   * @return Описание атрибута
   */
  String description() default "";

  /**
   * Настройки доступа
   *
   * @return Настройки доступа
   */
  String jpPackage() default "";

  /**
   * Признак идентификатора
   *
   * @return Да/Нет
   */
  boolean identifier() default false;

  /**
   * Признак обязательности
   *
   * @return Да/Нет
   */
  boolean mandatory() default false;

  /**
   * Тип атрибута
   *
   * @return Тип атрибута
   */
  JPType type();

  /**
   * Длина (для строковых полей)
   *
   * @return Длина
   */
  int length() default 0;

  /**
   * Кодовое имя класса, на который ссылается
   *
   * @return Кодовое имя класса, на который ссылается атрибут
   */
  String refJpClass() default "";

  /**
   * Кодовое имя атрибута ссылочного класса
   *
   * @return Кодовое имя атрибута ссылочного класса
   */
  String refJpAttr() default "";

  /**
   * Настройки хранения файла
   *
   * @return Настройки хранения файла
   */
  JPFile refJpFile() default @JPFile(storageCode = "", storageFilePath = "");

  /**
   * Настройки хранения простой дроби
   *
   * @return Настройки простой дроби
   */
  JPSimpleFraction simpleFraction() default @JPSimpleFraction(integerAttrCode = "", denominatorAttrCode = "");

  /**
   * Настройки хранения денежного типа
   *
   * @return Настройки денежного типа
   */
  JPMoney money() default @JPMoney(currency = "");

  /**
   * Настройки хранения пространственных данных
   *
   * @return Настройки пространственных данных
   */
  JPGeometry geometry() default @JPGeometry();

  /**
   * Путь виртуальной ссылки
   *
   * @return Путь виртуальной ссылки
   */
  String[] virtualReference() default {};

  /**
   * Тип виртуальной ссылки
   *
   * @return Тип виртуальной ссылки
   */
  JPType virtualType() default JPType.NONE;

  /**
   * Свойства псевдо-меты
   *
   * @return свойства псевдо-меты
   */
  JPProperty[] jpProps() default {};

  /**
   * Схемы вложенных свойств псевдо-меты
   *
   * @return вложенные свойства псевдо-меты
   */
  JPPropertySchema[] schemaProps() default {};

  /**
   * Код атрибута, содержащего подпись
   *
   * @return Код атрибута, содержащего подпись
   */
  String signAttrCode() default "";
}