package mp.jprime.meta.annotations;

import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация описания метакласса
 * Применяется к описанию наследника класса JPMeta
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Service
public @interface JPClass {
  /**
   * Глобальный идентификатор
   *
   * @return Глобальный идентификатор
   */
  String guid() default "";

  /**
   * Кодовое имя класса
   *
   * @return Кодовое имя класса
   */
  String code();

  /**
   * Множественный код кодового имени
   *
   * @return Множественный код кодового имени
   */
  String pluralCode();

  /**
   * Полный код класса
   *
   * @return Полный код класса
   */
  String qName();

  /**
   * Название класса
   *
   * @return Название класса
   */
  String name();

  /**
   * Короткое название класса
   *
   * @return Короткое название класса
   */
  String shortName() default "";

  /**
   * Описание класса
   *
   * @return Описание класса
   */
  String description() default "";

  /**
   * Настройки доступа
   *
   * @return Настройки доступа
   */
  String jpPackage() default "";

  /**
   * Признак класса только для внутренного доступа
   *
   * @return Признак класса только для внутренного доступа
   */
  boolean inner() default false;

  /**
   * Список атрибутов класса
   *
   * @return Список атрибутов класса
   */
  JPAttr[] attrs() default {};
}
