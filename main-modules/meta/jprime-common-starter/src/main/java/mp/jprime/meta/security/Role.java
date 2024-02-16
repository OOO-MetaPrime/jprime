package mp.jprime.meta.security;

import org.springframework.stereotype.Service;

/**
 * Описание ролей метанастройки и работа с ними
 */
@Service(value = "JPSecurityRoleConst")
public class Role extends mp.jprime.security.Role {
  /**
   * Конфигурирование: настройка метаописания
   */
  public final static String META_ADMIN = "META_ADMIN";

  /**
   * Конфигурирование: настройка метаописания
   *
   * @return Код роли
   */
  public String getMetaAdmin() {
    return META_ADMIN;
  }
}
