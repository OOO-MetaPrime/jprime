package mp.jprime.requesthistory;

import mp.jprime.requesthistory.constants.RequestPurpose;

import java.time.LocalDateTime;

/**
 * Данные истории запросов
 */
public interface RequestHistoryData {
  /**
   * Получить Идентификатор запроса
   *
   * @return Идентификатор запроса
   */
  String getUUID();

  /**
   * Получить код класса
   *
   * @return Код класса
   */
  String getClassCode();

  /**
   * Получить идентификатор пользователя
   *
   * @return Идентификатор пользователя
   */
  String getUserId();

  /**
   * Получить ip пользователя
   *
   * @return IP пользователя
   */
  String getUserIP();

  /**
   * Получить Логин пользователя
   *
   * @return Логин пользователя
   */
  String getUsername();

  /**
   * Получить Дату запроса
   *
   * @return Дата запроса
   */
  LocalDateTime requestDate();

  /**
   * Получить Цель запроса
   *
   * @return Цель запроса
   */
  RequestPurpose getPurpose();

  /**
   * Получить Идентификатор объекта
   *
   * @return Идентификатор объекта
   */
  String getObjectId();

  /**
   * Получить Тело запроса
   *
   * @return Тело запроса
   */
  String getRequestBody();
}
