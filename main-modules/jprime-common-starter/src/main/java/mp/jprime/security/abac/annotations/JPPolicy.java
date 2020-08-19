package mp.jprime.security.abac.annotations;

import mp.jprime.dataaccess.JPAction;

/**
 * Политика
 */
public @interface JPPolicy {
  /**
   * Название правила
   *
   * @return Название правила
   */
  String name();

  /**
   * QName правила
   *
   * @return qName правила
   */
  String qName() default "";

  /**
   * Действия
   *
   * @return Действия
   */
  JPAction[] actions();

  /**
   * Возвращает правила для пользователя
   *
   * @return Правила для пользователя
   */
  JPSubjectRule[] subjectRules() default {};

  /**
   * Возвращает правила для объекта
   *
   * @return Правила для объекта
   */
  JPResourceRule[] resourceRules() default {};

  /**
   * Возвращает правила для окружения
   *
   * @return Правила для окружения
   */
  JPEnviromentRule[] enviromentRules() default {};
}
