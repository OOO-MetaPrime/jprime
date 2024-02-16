package mp.jprime.common.annotations;

import mp.jprime.meta.beans.JPType;

/**
 * Аннотация описания параметра
 */
public @interface JPParam {
  /**
   * Кодовое имя параметра
   *
   * @return Кодовое имя параметра
   */
  String code();

  /**
   * Полный код параметра
   *
   * @return Полный код параметра
   */
  String qName() default "";

  /**
   * Описание параметра
   *
   * @return Описание параметра
   */
  String description();

  /**
   * Признак обязательности
   *
   * @return Да/Нет
   */
  boolean mandatory() default false;

  /**
   * Тип параметра
   *
   * @return Тип параметра
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

  /**
   * Клиентский поиск
   *
   * @return Да/Нет
   */
  boolean clientSearch() default false;
}
