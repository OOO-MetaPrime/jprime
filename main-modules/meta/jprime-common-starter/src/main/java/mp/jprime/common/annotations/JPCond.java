package mp.jprime.common.annotations;

/**
 * Условие
 */
public @interface JPCond {
  /**
   * IN
   *
   * @return IN
   */
  String[] in() default {};

  /**
   * Not IN
   *
   * @return Not IN
   */
  String[] notIn() default {};

  /**
   * Is Null
   *
   * @return Is Null
   */
  boolean isNull() default false;

  /**
   * Is Not Null
   */
  boolean isNotNull() default false;
}
