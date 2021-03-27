package mp.jprime.common.annotations;

/**
 * Путь атрибута
 */
public @interface JPClassAttr {
  /**
   * Кодовое имя класса
   *
   * @return Кодовое имя класса
   */
  String jpClass();

  /**
   * Кодовое имя атрибута
   *
   * @return Кодовое имя атрибута
   */
  String jpAttr();
}
