package mp.jprime.nsi;

/**
 * Каталог НСИ
 */
public interface JpNsiCatalog {
  /**
   * Код каталога
   *
   * @return Код каталога
   */
  String getCatalog();

  /**
   * Название каталога
   *
   * @return Название
   */
  String getName();

  /**
   * Описание каталога
   *
   * @return Описание каталога
   */
  String getDescription();
}
