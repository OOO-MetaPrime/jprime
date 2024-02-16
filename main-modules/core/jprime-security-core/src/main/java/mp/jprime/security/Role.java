package mp.jprime.security;

import org.springframework.stereotype.Service;

/**
 * Описание ролей и работа с ними
 */
@Service(value = "JPRoleConst")
public class Role {
  /**
   * Данные: Базовый пользователь
   */
  public static final String AUTH_ACCESS = "AUTH_ACCESS";
  /**
   * Данные: Администратор прикладных данных
   */
  public static final String ADMIN = "ADMIN";
  /**
   * Конфигурирование: настройка экспорта/импорта данных
   */
  public static final String IMEX_ADMIN = "IMEX_ADMIN";
  /**
   * Конфигурирование: настройка UI
   */
  public final static String UI_ADMIN = "UI_ADMIN";
  /**
   * Конфигурирование: непроверенные возможности
   */
  public static final String FEATURES_ADMIN = "FEATURES_ADMIN";

  /**
   * Данные: Базовый пользователь
   *
   * @return Код роли
   */
  public String getAuthAccess() {
    return AUTH_ACCESS;
  }

  /**
   * Данные: Администратор прикладных данных
   *
   * @return Код роли
   */
  public String getAdmin() {
    return ADMIN;
  }

  /**
   * Конфигурирование: настройка экспорта/импорта данных
   *
   * @return Код роли
   */
  public String getImexAdmin() {
    return IMEX_ADMIN;
  }

  /**
   * Конфигурирование: настройка UI
   *
   * @return Код роли
   */
  public String getUiAdmin() {
    return UI_ADMIN;
  }

  /**
   * Конфигурирование: непроверенные возможности
   *
   * @return Код роли
   */
  public String getFeaturesAdmin() {
    return FEATURES_ADMIN;
  }
}
