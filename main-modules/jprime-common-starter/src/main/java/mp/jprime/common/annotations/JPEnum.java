package mp.jprime.common.annotations;

public @interface JPEnum {
  /**
   * Значение
   *
   * @return Значение
   */
  String value();

  /**
   * Название значения
   *
   * @return Название
   */
  String description();

  /**
   * Уникальный qName
   *
   * @return Уникальный qName
   */
  String qName() default "";
}
