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
}
