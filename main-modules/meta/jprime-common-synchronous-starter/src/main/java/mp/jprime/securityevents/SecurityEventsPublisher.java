package mp.jprime.securityevents;

import mp.jprime.security.AuthInfo;

/**
 * Работа с отправкой событий безопасности
 */
public interface SecurityEventsPublisher {
  /**
   * Отправка события
   *
   * @param code      Код события
   * @param name      Название события
   * @param success   Признак успешности
   * @param classCode Код класса
   * @param objectId  Идентификатор объекта
   * @param body      Тело события
   * @param username  Пользователь
   * @param userId    userId
   * @param userIP    userIP
   */
  void sendEvent(String code, String name, boolean success,
                 String classCode, String objectId, String body,
                 String username, String userId, String userIP);

  /**
   * Отправка события
   *
   * @param code     Код события
   * @param name     Название события
   * @param success  Признак успешности
   * @param username Пользователь
   * @param userId   userId
   * @param userIP   userIP
   */
  default void sendEvent(String code, String name, boolean success, String username, String userId, String userIP) {
    sendEvent(code, name, success, null, null, null, username, userId, userIP);
  }

  /**
   * Отправка события
   *
   * @param code      Код события
   * @param name      Название события
   * @param success   Признак успешности
   * @param classCode Код класса
   * @param objectId  Идентификатор объекта
   * @param body      Тело события
   * @param auth      AuthInfo
   */
  void sendEvent(String code, String name, boolean success, String classCode, String objectId, String body, AuthInfo auth);

  /**
   * Отправка события
   *
   * @param code    Код события
   * @param name    Название события
   * @param success Признак успешности
   * @param auth    AuthInfo
   */
  default void sendEvent(String code, String name, boolean success, AuthInfo auth) {
    sendEvent(code, name, success, null, null, null, auth);
  }
}
