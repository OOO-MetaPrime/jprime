package mp.jprime.dataaccess.addinfos;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.security.AuthInfo;

/**
 * Параметры для сведений об объекте
 */
public interface JPObjectAddInfoParams {
  /**
   * Идентификатор объекта
   *
   * @return Идентификатор объекта
   */
  Object getId();

  /**
   * Кодовое имя класса объекта
   *
   * @return Кодовое имя класса объекта
   */
  String getJpClassCode();

  /**
   * Данные авторизации
   * Могут быть не указаны
   *
   * @return Данные авторизации
   */
  AuthInfo getAuth();

  /**
   * Источник данных
   *
   * @return Источник данных
   */
  Source getSource();
}
