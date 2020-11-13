package mp.jprime.common.annotations;

import mp.jprime.meta.beans.JPType;

/**
 * Аннотация описания параметра
 */
public @interface JPParam {
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
   * Описание атрибута
   *
   * @return Описание атрибута
   */
  String description();

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
   * JSON для выборки объектов
   *
   * @return JSON для выборки объектов
   */
  String refFilter() default "";

  /**
   * Разрешен множественный выбор
   *
   * @return Да/Нет
   */
  boolean multiple() default false;
  /**
   * Список перечислимых значений
   *
   * @return Список перечислимых значений
   */
  JPEnum[] enums() default {};
}
