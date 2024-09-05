package mp.jprime.security;

/**
 * Данные авторизации
 */
public interface AuthInfo extends AuthParams {
  /**
   * Возвращает полностью токен
   *
   * @return Токен
   */
  String getToken();
}
