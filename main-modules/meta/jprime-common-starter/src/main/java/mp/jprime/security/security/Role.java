package mp.jprime.security.security;

/**
 * Описание ролей настроек доступа и работа с ними
 */
public class Role extends mp.jprime.security.Role {
  /**
   * Конфигурирование: настройка прав доступа
   */
  public final static String AUTH_ADMIN = "AUTH_ADMIN";
  /**
   * Конфигурирование: создание/редактирование внутри организации
   */
  public final static String AUTH_ORG_ADMIN  = "AUTH_ORG_ADMIN";
}

