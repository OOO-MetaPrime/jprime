package mp.jprime.requesthistory.security;

import org.springframework.stereotype.Service;

/**
 * Описание ролей и работа с ними
 */
@Service(value = "JPRequestHistoryRoleConst")
public class Role extends mp.jprime.security.Role {
  /**
   * Данные: История запросов
   */
  public final static String REQUEST_HISTORY_ADMIN = "REQUEST_HISTORY_ADMIN";

  /**
   * Данные: История запросов
   *
   * @return Код роли
   */
  public String getRequestHistoryAdmin() {
    return REQUEST_HISTORY_ADMIN;
  }
}
