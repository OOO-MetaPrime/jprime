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
   * Конфигурирование: создание/редактирование внутри организации
   */
  public final static String AUTH_ORG_ADMIN  = "AUTH_ORG_ADMIN";

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
}

