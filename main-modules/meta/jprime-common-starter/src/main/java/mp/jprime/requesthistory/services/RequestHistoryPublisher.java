package mp.jprime.requesthistory.services;

import mp.jprime.security.AuthInfo;
import org.springframework.web.server.ServerWebExchange;

/**
 * Работа с отправкой Истории запросов
 */
public interface RequestHistoryPublisher {
  /**
   * Отправить find-запрос RequestHistoryEvent
   *
   * @param classCode Код класса
   * @param objectId  Идентификатор объекта
   * @param authInfo  Данные аутентификации
   * @param swe       Данные запроса
   */
  void sendFind(String classCode, Object objectId, AuthInfo authInfo, ServerWebExchange swe);

  /**
   * Отправить search-запрос RequestHistoryEvent
   *
   * @param classCode   Код класса
   * @param requestBody Тело запроса
   * @param authInfo    Данные аутентификации
   * @param swe         Данные запроса
   */
  void sendSearch(String classCode, Object requestBody, AuthInfo authInfo, ServerWebExchange swe);
}
