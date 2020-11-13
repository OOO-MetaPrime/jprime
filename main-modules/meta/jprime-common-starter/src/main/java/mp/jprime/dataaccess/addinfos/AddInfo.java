package mp.jprime.dataaccess.addinfos;

/**
 * Дополнительные сведения об объекте
 */
public interface AddInfo {
  /**
   * Код сведений
   *
   * @return Код сведений
   */
  String getCode();

  /**
   * Информация
   *
   * @return Информация
   */
  String getInfo();
}
