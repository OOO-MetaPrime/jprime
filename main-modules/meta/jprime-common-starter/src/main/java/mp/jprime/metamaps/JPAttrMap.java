package mp.jprime.metamaps;

import mp.jprime.metamaps.beans.JPCase;

/**
 * описание привязки атрибута к хранилищу
 */
public interface JPAttrMap {
  /**
   * Кодовое имя атрибута
   *
   * @return Кодовое имя атрибута
   */
  String getCode();

  /**
   * Мап на хранилище
   *
   * @return Мап на хранилище
   */
  String getMap();

  /**
   * Мап на поле с индексами нечеткого поиска
   *
   * @return Мап на поле с индексами нечеткого поиска
   */
  String getFuzzyMap();

  /**
   * Возвращает регистр значений
   *
   * @return Регистр значений
   */
  String getCs();

  /**
   * Регистр значений
   *
   * @return Регистр значений
   */
  JPCase getCase();

  /**
   * Запрет на изменение значений
   *
   * @return Да/Нет
   */
  boolean isReadOnly();
}