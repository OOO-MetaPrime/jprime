package mp.jprime.dataaccess.applyvalues;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.security.AuthInfo;

import java.util.Collection;

/**
 * Параметры для дополнения значений
 */
public interface JPObjectApplyValueParams {
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
   * Данные объекта
   *
   * @return Данные объекта
   */
  JPData getData();

  /**
   * Кодовые имена атрибутов класса, которые были модифицированы
   *
   * @return Список кодовых имен атрибутов
   */
  Collection<String> getAttrs();

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
