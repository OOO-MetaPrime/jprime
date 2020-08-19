package mp.jprime.security.abac.annotations;

/**
 * Группа политик
 */
public @interface JPPolicySet {
  /**
   * Название группы
   *
   * @return Название группы
   */
  String name();

  /**
   * QName группы
   *
   * @return qName группы
   */
  String qName() default "";

  /**
   * Список классов для применения политики
   *
   * @return Список классов
   */
  String[] jpClasses() default {};

  /**
   * Политики
   *
   * @return Политики
   */
  JPPolicy[] policies();
}
