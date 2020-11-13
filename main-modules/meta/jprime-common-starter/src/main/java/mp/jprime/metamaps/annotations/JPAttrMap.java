package mp.jprime.metamaps.annotations;

import mp.jprime.metamaps.beans.JPCase;

/**
 * Аннотация описания маппинга метаатрибута
 */
public @interface JPAttrMap {

  /**
   * Кодовое имя атрибута
   *
   * @return Кодовое имя атрибута
   */
  String code();

  /**
   * Мап на хранилище
   *
   * @return Мап на хранилище
   */
  String map();

  /**
   * Мап на поле с индексами нечеткого поиска
   *
   * @return Мап на поле с индексами нечеткого поиска
   */
  String fuzzyMap() default "";

  /**
   * Регистр значений
   *
   * @return Регистр значений
   */
  JPCase cs() default JPCase.ANY;

  /**
   * Запрет на изменение значений
   *
   * @return Да/Нет
   */
  boolean readOnly() default false;
}