package mp.jprime.security;

/**
 * Описание ролей и работа с ними
 */
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
}
