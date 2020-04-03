package mp.jprime.common.annotations;

public @interface JPEnum {
  /**
   * Значение
   *
   * @return Значение
   */
  String getValue();

  /**
   * Название значения
   *
   * @return Название
   */
  String getDescription();

  /**
   * Уникальный qName
   *
   * @return Уникальный qName
   */
  String getQName() default "";
}
