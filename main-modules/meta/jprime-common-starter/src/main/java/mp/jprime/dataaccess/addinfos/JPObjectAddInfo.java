package mp.jprime.dataaccess.addinfos;

import java.util.Collection;

/**
 * Дополнительные сведения об объекте
 */
public interface JPObjectAddInfo {
  /**
   * Дополняет значения по-умолчанию
   *
   * @param params Параметры для вычисления значений по-умолчанию*
   */
  Collection<AddInfo> getAddInfo(JPObjectAddInfoParams params);
}
