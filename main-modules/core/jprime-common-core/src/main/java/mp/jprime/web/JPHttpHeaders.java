package mp.jprime.web;

/**
 * Заголовки, характерные JPrime
 */
public class JPHttpHeaders {
  /**
   * Заголовок для передачи разрешенных системой ролей
   */
  public static final String X_JPRIME_ALLOWROLES = "X-JPrime-AllowRoles";

  /**
   * Заголовок для передачи имени файла
   */
  public static final String X_JPRIME_FILE_TITLE = "X-JPrime-File-Title";

  /**
   * Заголовок для передачи имени клиента авторизации
   */
  public static final String X_JPRIME_AUTH_CLIENT = "X-JPrime-Auth-Client";
  /**
   * Заголовок для запрета сохранения истории запросов
   */
  public static final String X_JPRIME_WEB_QUERY_HISTORY_DISABLED = "X-JPrime-WebQuery-History-Disabled";
  /**
   * Заголовок для передачи кода подсистемы для кросс-системных запросов
   */
  public static final String X_JPRIME_SYSTEM_CODE = "X-JPrime-System-Code";
}
