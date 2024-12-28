package mp.jprime.requesthistory;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Фильтр для поиска истории запросов
 */
public interface RequestHistoryFilter {
  /**
   * Получить Код класса
   *
   * @return Код класса
   */
  Collection<String> getClassCode();

  /**
   * Получить Идентификатор пользователя
   *
   * @return Идентификатор пользователя
   */
  Collection<String> getUserId();

  /**
   * Получить Логин пользователя
   *
   * @return Логин пользователя
   */
  Collection<String> getUsername();

  /**
   * Получить Дату запроса с
   *
   * @return Дата запроса с
   */
  LocalDateTime getRequestDateFrom();

  /**
   * Получить Дату запроса по
   *
   * @return Дата запроса по
   */
  LocalDateTime getRequestDateTo();

  /**
   * Получить Идентификатор объекта
   *
   * @return Идентификатор объекта
   */
  Collection<String> getObjectId();
}
