package mp.jprime.security;

/**
 * Обработчик аутентификации
 */
public interface AuthConvertor<T extends AuthInfo> {
  /**
   * Создает описание аутентификации
   *
   * @param json Строка
   * @return Описание аутентификации
   */
  T getQuery(String json);

  /**
   * Создает описание аутентификации
   *
   * @param info AuthInfo
   * @return Описание аутентификации
   */
  String toString(T info);
}
