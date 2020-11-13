package mp.jprime.dataaccess.defvalues;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.security.AuthInfo;

/**
 * Параметры для вычисления значений по-умолчанию
 */
public interface JPObjectDefValueParams {
  /**
   * Идентификатор объекта, из которого создаем
   * Может быть не указан
   *
   * @return Идентификатор объекта, из которого создаем
   */
  Object getRootId();

  /**
   * Кодовое имя класса объекта, из которого создаем
   * Может быть не указан
   *
   * @return Кодовое имя класса объекта, из которого создаем
   */
  String getRootJpClassCode();

  /**
   * Данные объекта, из которого создаем
   * Могут быть не указаны
   *
   * @return Данные объекта, из которого создаем
   */
  JPData getRootData();

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
