package mp.jprime.security;

import java.util.Collection;

/**
 * Данные авторизации
 */
public interface AuthInfo extends ConnectionInfo {
  /**
   * Возвращает идентификатор пользователя
   *
   * @return идентификатор пользователя
   */
  String getUserId();

  /**
   * Возвращает организацию пользователя
   *
   * @return организацию пользователя
   */
  String getOrgId();

  /**
   * Возвращает подразделение пользователя
   *
   * @return подразделение пользователя
   */
  String getDepId();

  /**
   * Возвращает имя пользователя
   *
   * @return Имя пользователя
   */
  String getUsername();

  /**
   * Возвращает роли пользователя
   *
   * @return Роли
   */
  Collection<String> getRoles();

  /**
   * Возвращает полностью токен
   *
   * @return Токен
   */
  String getToken();
}
