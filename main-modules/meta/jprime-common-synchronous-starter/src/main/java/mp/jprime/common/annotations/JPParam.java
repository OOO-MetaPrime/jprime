package mp.jprime.common.annotations;

import mp.jprime.files.FileType;
import mp.jprime.formats.JPStringFormat;
import mp.jprime.meta.annotations.JPMoney;
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
   * Тип строкового поля
   *
   * @return Тип строкового поля
   */
  JPStringFormat stringFormat() default JPStringFormat.NONE;

  /**
   * Типы файлов для выбора
   *
   * @return Типы файлов
   */
  FileType[] fileTypes() default {};

  /**
   * Маска строкового поля
   *
   * @return Маска строкового поля
   */
  String stringMask() default "";

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
   * Настройки хранения денежного типа
   *
   * @return Настройки денежного типа
   */
  JPMoney money() default @JPMoney(currency = "");

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

  /**
   * Признак логирования значения
   *
   * @return Признак логирования значения
   */
  boolean actionLog() default true;

  /**
   * Признак только для чтения
   *
   * @return Признак только для чтения
   */
  boolean readOnly() default false;
}
