package mp.jprime.security.security;

import org.springframework.stereotype.Service;

/**
 * Описание ролей настроек доступа и работа с ними
 */
@Service(value = "JPAuthRoleConst")
public class Role extends mp.jprime.security.Role {
  /**
   * Конфигурирование: настройка прав доступа
   */
  public final static String AUTH_ADMIN = "AUTH_ADMIN";
  /**
   * Конфигурирование: Администратор с правами на управление пользователями
   */
  public final static String AUTH_USER_ADMIN = "AUTH_USER_ADMIN";
  /**
   * Конфигурирование: создание/редактирование внутри организации
   */
  public final static String AUTH_ORG_ADMIN  = "AUTH_ORG_ADMIN";
  /**
   * Конфигурирование: создание/редактирование внутри обособленного подразделения
   */
  public final static String AUTH_SEPDEP_ADMIN  = "AUTH_SEPDEP_ADMIN";
  /**
   * Конфигурирование: Администратор журнала безопасности
   */
  public final static String SECURITY_SETTINGS_ADMIN = "SECURITY_SETTINGS_ADMIN";

  /**
   * ДКонфигурирование: настройка прав доступа
   *
   * @return Код роли
   */
  public String getAuthAdmin() {
    return AUTH_ADMIN;
  }

  /**
   * Конфигурирование: создание/редактирование внутри организации
   *
   * @return Код роли
   */
  public String getAuthOrgAdmin() {
    return AUTH_ORG_ADMIN;
  }

  /**
   * Конфигурирование: создание/редактирование внутри обособленного подразделения
   *
   * @return Код роли
   */
  public String getAuthSepDepAdmin() {
    return AUTH_SEPDEP_ADMIN;
  }
}

