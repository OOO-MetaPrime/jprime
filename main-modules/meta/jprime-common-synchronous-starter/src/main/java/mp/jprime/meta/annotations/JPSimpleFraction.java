package mp.jprime.meta.annotations;

/**
 * Описание простой дроби
 */
public @interface JPSimpleFraction {
  /**
   * Атрибут для хранения - Целая часть дроби
   *
   * @return Кодовое имя атрибута
   */
  String integerAttrCode() default "";

  /**
   * Атрибут для хранения - Знаменатель дроби
   *
   * @return Кодовое имя атрибута
   */
  String denominatorAttrCode();
}