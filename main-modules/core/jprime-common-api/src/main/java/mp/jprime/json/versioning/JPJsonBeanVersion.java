package mp.jprime.json.versioning;

/**
 * Данные в формате указанной версии
 */
public interface JPJsonBeanVersion<T> {
  /**
   * Номер версии
   *
   * @return Номер версии
   */
  Integer getVersion();

  /**
   * Данные в формате версии
   *
   * @return Данные в формате бина
   */
  T getData();
}
