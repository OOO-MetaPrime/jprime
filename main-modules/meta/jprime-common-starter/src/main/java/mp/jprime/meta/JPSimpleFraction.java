package mp.jprime.meta;

/**
 * Метаописание хранения простой дроби
 */
public interface JPSimpleFraction {
  /**
   * Атрибут для хранения - Целая часть дроби
   *
   * @return Кодовое имя атрибута
   */
  String getIntegerAttrCode();

  /**
   * Атрибут для хранения - числитель дроби
   *
   * @return Кодовое имя атрибута
   */
  String getNumeratorAttrCode();

  /**
   * Атрибут для хранения - Знаменатель дроби
   *
   * @return Кодовое имя атрибута
   */
  String getDenominatorAttrCode();
}
