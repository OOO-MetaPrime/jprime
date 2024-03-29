package mp.jprime.events.systemevents;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Системное событие
 */
public interface JPSystemEvent {
  /**
   * Код инициатора события
   *
   * @return Код инициатора события
   */
  String getProducer();

  /**
   * Код слушателя, которому адресовано событие (опционально)
   *
   * @return Код слушателя
   */
  String getConsumer();

  /**
   * Дата события
   *
   * @return Дата события
   */
  LocalDateTime getEventDate();

  /**
   * Код события
   *
   * @return Код события
   */
  String getEventCode();

  /**
   * Признак внешнего события (можно пересылать за пределы системы, в UI например)
   *
   * @return Признак внешнего события (можно пересылать за пределы системы, в UI например)
   */
  boolean isExternal();

  /**
   * Дополнительные свойства
   *
   * @return Дополнительные свойства
   */
  Map<String, String> getData();
}
