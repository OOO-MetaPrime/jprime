package mp.jprime.meta.annotations;

import mp.jprime.beans.JPPropertyType;

/**
 * Аннотация описания свойства псевдо-меты
 */
public @interface JPProperty {

  /**
   * Кодовое имя свойства
   *
   * @return Кодовое имя свойства
   */
  String code();

  /**
   * Полный код свойства
   *
   * @return Полный код свойства
   */
  String qName();

  /**
   * Название свойства
   *
   * @return Название свойства
   */
  String name();

  /**
   * Короткое название свойства
   *
   * @return Короткое название свойства
   */
  String shortName() default "";

  /**
   * Описание свойства
   *
   * @return Описание свойства
   */
  String description() default "";

  /**
   * Признак обязательности
   *
   * @return Да/Нет
   */
  boolean mandatory() default false;

  /**
   * Тип свойства
   *
   * @return Тип свойства
   */
  JPPropertyType type();

  /**
   * Длина (для строковых полей)
   *
   * @return Длина
   */
  int length() default 0;

  /**
   * Кодовое имя класса, на который ссылается
   *
   * @return Кодовое имя класса, на который ссылается свойство
   */
  String refJpClass() default "";

  /**
   * Кодовое имя атрибута ссылочного класса
   *
   * @return Кодовое имя атрибута ссылочного класса
   */
  String refJpAttr() default "";

  /**
   * Код описания вложенных свойств {@link JPPropertySchema}
   *
   * @return Код описания вложенных свойств {@link JPPropertySchema}
   */
  String schemaCode() default "";
}
