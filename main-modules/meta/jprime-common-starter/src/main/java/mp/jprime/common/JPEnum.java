package mp.jprime.common;

/**
 * описания значения
 */
public interface JPEnum {
  /**
   * Значение
   *
   * @return Значение
   */
  Object getValue();

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
  String getQName();
}
