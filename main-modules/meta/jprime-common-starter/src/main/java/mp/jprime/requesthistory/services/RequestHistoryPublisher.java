package mp.jprime.requesthistory.services;

import mp.jprime.requesthistory.RequestHistoryData;
import mp.jprime.security.AuthInfo;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Работа с отправкой Истории запросов
 */
public interface RequestHistoryPublisher {
  /**
   * Асинхронная отправка данных
   *
   * @param data Данные истории запросов
   * @param swe  Данные запроса
   */
  default Mono<Void> asyncSend(RequestHistoryData data, ServerWebExchange swe) {
    return Mono.create(x -> {
      send(data, swe);
      x.success();
    });
  }

  /**
   * Отправка данных
   *
   * @param data Данные истории запросов
   * @param swe  Данные запроса
   */
  default void send(RequestHistoryData data, ServerWebExchange swe) {
    send(data, swe, Boolean.TRUE);
  }

  /**
   * Асинхронная отправка данных
   *
   * @param data      Данные истории запросов
   * @param swe       Данные запроса
   * @param checkUser Проверять данные пользователя
   */
  default Mono<Void> asyncSend(RequestHistoryData data, ServerWebExchange swe, boolean checkUser) {
    return Mono.create(x -> {
      send(data, swe, checkUser);
      x.success();
    });
  }

  /**
   * Отправка данных
   *
   * @param data      Данные истории запросов
   * @param swe       Данные запроса
   * @param checkUser Проверять данные пользователя
   */
  void send(RequestHistoryData data, ServerWebExchange swe, boolean checkUser);

  /**
   * Отправить find-запрос RequestHistoryData
   *
   * @param classCode Код класса
   * @param objectId  Идентификатор объекта
   * @param authInfo  Данные аутентификации
   * @param swe       Данные запроса
   */
  void sendFind(String classCode, Object objectId, AuthInfo authInfo, ServerWebExchange swe);

  /**
   * Отправить search-запрос RequestHistoryData
   *
   * @param classCode   Код класса
   * @param requestBody Тело запроса
   * @param authInfo    Данные аутентификации
   * @param swe         Данные запроса
   */
  void sendSearch(String classCode, Object requestBody, AuthInfo authInfo, ServerWebExchange swe);
}
