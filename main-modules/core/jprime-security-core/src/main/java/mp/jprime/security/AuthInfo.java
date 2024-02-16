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
   * Возвращает ОКТМО пользователя
   *
   * @return ОКТМО пользователя
   */
  String getOktmo();

  /**
   * Возвращает ведомство пользователя
   *
   * @return ведомство пользователя
   */
  String getAdministration();

  /**
   * Возвращает предметные группы пользователя
   *
   * @return предметные группы пользователя
   */
  Collection<Integer> getSubjectGroups();

  /**
   * Возвращает организацию пользователя
   *
   * @return организация пользователя
   */
  String getOrgId();

  /**
   * Возвращает подразделение пользователя
   *
   * @return подразделение пользователя
   */
  String getDepId();

  /**
   * Возвращает логин пользователя
   *
   * @return логин пользователя
   */
  String getUsername();

  /**
   * Возвращает ФИО пользователя
   *
   * @return ФИО пользователя
   */
  String getFio();

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
