package mp.jprime.json.versioning;

import mp.jprime.lang.JPJsonNode;

/**
 * Данные в формате указанной версии
 */
public interface JPJsonVersion {
  /**
   * Номер версии
   *
   * @return Номер версии
   */
  Integer getVersion();

  /**
   * Данные в формате версии
   *
   * @return Данные в формате json
   */
  JPJsonNode getData();
}
