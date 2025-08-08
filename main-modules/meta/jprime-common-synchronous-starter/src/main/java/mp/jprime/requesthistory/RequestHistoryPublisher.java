package mp.jprime.requesthistory;

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
   * @param auth      Данные аутентификации
   * @param swe       Данные запроса
   * @param classCode Код класса
   * @param objectId  Идентификатор объекта
   * @param result    Результат поиска
   */
  void sendObject(AuthInfo auth, ServerWebExchange swe, String classCode, Object objectId, Supplier<RequestHistoryObject> resultFunc);

  /**
   * Отправить search-запрос RequestHistoryEvent
   *
   * @param auth           Данные аутентификации
   * @param swe            Данные запроса
   * @param classCode      Код класса
   * @param searchBodyFunc Тело запроса
   * @param resultFunc     Результаты поиска
   */
  void sendSearch(AuthInfo auth, ServerWebExchange swe, String classCode, Supplier<Object> searchBodyFunc, Supplier<Collection<RequestHistoryObject>> resultFunc);
}
