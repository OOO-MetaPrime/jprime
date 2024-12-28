package mp.jprime.dataaccess.addinfos;

import java.util.Collection;

/**
 * Логика работы с дополнительными сведениями об объекте
 */
public interface JPObjectAddInfoProvider {
  /**
   * Возвращает сведения об объекте
   *
   * @param params Параметры для получения сведений
   */
  Collection<AddInfo> getAddInfo(JPObjectAddInfoParams params);
}
