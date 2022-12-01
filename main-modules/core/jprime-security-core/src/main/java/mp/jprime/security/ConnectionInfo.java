package mp.jprime.security;

/**
 * Информация о соединении
 */
public interface ConnectionInfo {
  /**
   * Возвращает IP пользователя
   *
   * @return IP пользователя
   */
  String getUserIP();

  /**
   * Возвращает имя пользователя
   *
   * @return Имя пользователя
   */
  String getUsername();
}
