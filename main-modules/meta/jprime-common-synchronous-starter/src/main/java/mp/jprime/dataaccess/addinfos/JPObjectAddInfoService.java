package mp.jprime.dataaccess.addinfos;

import mp.jprime.dataaccess.addinfos.beans.JPObjectAddInfoParamsBean;
import mp.jprime.security.AuthInfo;
import java.util.Collection;

/**
 * Логика работы с дополнительными сведениями об объекте
 */
public interface JPObjectAddInfoService {
  /**
   * Возвращает сведения об объекте
   *
   * @param params Параметры для получения сведений
   */
  Collection<AddInfo> getAddInfo(JPObjectAddInfoParams params);


  /**
   * Возвращает сведения об объекте
   *
   * @param jpClassCode Кодовое имя класса объекта
   * @param id          Идентификатор объекта
   * @param auth        Данные авторизации
   */
  default Collection<AddInfo> getAddInfo(String jpClassCode, Object id, AuthInfo auth) {
    return getAddInfo(JPObjectAddInfoParamsBean.newBuilder(jpClassCode, id).auth(auth).build());
  }
}
