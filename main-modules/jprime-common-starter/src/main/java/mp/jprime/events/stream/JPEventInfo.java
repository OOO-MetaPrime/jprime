package mp.jprime.events.stream;

/**
 * Данные события
 *
 * @param <T> Тип данных события
 */
public interface JPEventInfo<T extends JsonEvent> {
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
