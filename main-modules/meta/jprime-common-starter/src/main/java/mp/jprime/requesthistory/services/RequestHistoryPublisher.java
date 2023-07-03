package mp.jprime.requesthistory.services;

import mp.jprime.requesthistory.RequestHistoryObject;
import mp.jprime.security.AuthInfo;
import org.springframework.web.server.ServerWebExchange;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * Работа с отправкой Истории запросов
 */
public interface RequestHistoryPublisher {
  /**
   * Формирует toRequestHistoryObject
   *
   * @param classCode Код класса
   * @param objectId  Идентификатор объекта
   * @param body      Тело истории
   * @return RequestHistoryObject
   */
  RequestHistoryObject toRequestHistoryObject(String classCode, Object objectId, Object body);

  /**
   * Отправить find-запрос RequestHistoryEvent
   *
   * @param authInfo  Данные аутентификации
   * @param swe       Данные запроса
   * @param classCode Код класса
   * @param objectId  Идентификатор объекта
   * @param result    Результат поиска
   */
  void sendObject(AuthInfo authInfo, ServerWebExchange swe, String classCode, Object objectId, Supplier<RequestHistoryObject> result);

  /**
   * Отправить search-запрос RequestHistoryEvent
   *
   * @param authInfo   Данные аутентификации
   * @param swe        Данные запроса
   * @param classCode  Код класса
   * @param searchBody Тело запроса
   * @param results    Результаты поиска
   */
  void sendSearch(AuthInfo authInfo, ServerWebExchange swe, String classCode, Supplier<Object> searchBody, Supplier<Collection<RequestHistoryObject>> results);
}
