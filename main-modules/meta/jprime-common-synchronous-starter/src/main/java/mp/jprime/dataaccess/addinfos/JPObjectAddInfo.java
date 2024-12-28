package mp.jprime.dataaccess.addinfos;

import java.util.Collection;

/**
 * Дополнительные сведения об объекте
 */
public interface JPObjectAddInfo {
  /**
   * Дополняет сведения об объекте
   *
   * @param params Параметры для вычисления сведений об объекте
   */
  Collection<AddInfo> getAddInfo(JPObjectAddInfoParams params);
}
