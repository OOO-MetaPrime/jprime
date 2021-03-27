package mp.jprime.utils.annotations;

import mp.jprime.common.JPAppendType;
import mp.jprime.common.annotations.JPClassAttr;
import mp.jprime.common.annotations.JPParam;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для отметки шагов утилиты
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JPUtilModeLink {
  /**
   * Настройки доступа
   *
   * @return Настройки доступа
   */
  String jpPackage() default "";
  /**
   * Роли, имеющиеся доступ к этому шагу
   *
   * @return Список ролей
   */
  String[] authRoles() default {};

  /**
   * Кодовое имя шага
   *
   * @return Кодовое имя шага
   */
  String code();

  /**
   * Название шага
   *
   * @return Название шага
   */
  String title();

  /**
   * QName утилиты
   *
   * @return QName утилиты
   */
  String qName() default "";

  /**
   * Признак логирования действий
   *
   * @return Признак логирования действи
   */
  boolean actionLog() default true;

  /**
   * Тип доступности шага
   *
   * @return Тип доступности шага
   */
  JPAppendType type() default JPAppendType.CUSTOM;

  /**
   * Настройки доступа на атрибутах
   *
   * @return Настройки доступа на атрибутах
   */
  JPClassAttr[] jpAttrs() default {};

  /**
   * Сообщение перед запуском шага
   */
  String confirm() default "";

  /**
   * Список входящих параметров
   *
   * @return Список входящих параметров
   */
  JPParam[] inParams() default {};

  /**
   * Выходной класс параметров
   *
   * @return Выходной класс параметров
   */
  Class<?> outClass();

  /**
   * Список кастомных исходящих параметров
   *
   * @return Список кастомных исходящих параметров
   */
  JPParam[] outCustomParams() default {};
}
