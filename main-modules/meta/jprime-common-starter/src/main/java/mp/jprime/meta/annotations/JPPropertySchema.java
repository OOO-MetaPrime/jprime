package mp.jprime.meta.annotations;

/**
 * Аннотация описания схемы вложенных свойств псевдо-меты
 */
public @interface JPPropertySchema {
  /**
   * Кодовое имя схемы
   *
   * @return Кодовое имя схемы
   */
  String code();

  /**
   * Схема вложенных свойств
   *
   * @return список вложенных свойств
   */
  JPProperty[] jpProps();
}
