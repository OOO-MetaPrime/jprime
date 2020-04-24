package mp.jprime.events.stream;

import java.time.LocalDateTime;

/**
 * Данные события
 *
 * @param <T> Тип данных события
 */
public interface JPEventInfo<T extends JsonEvent> {
  /**
   * Дата события
   *
   * @return Дата события
   */
  LocalDateTime getDate();
  /**
   * Тип события
   *
   * @return Тип события
   */
  String getEventType();

  /**
   * Данные события
   *
   * @return Данные события
   */
  T getEventData();
}
