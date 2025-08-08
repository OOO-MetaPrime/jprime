package mp.jprime.dataaccess.transaction.events;

import mp.jprime.dataaccess.Event;
import mp.jprime.security.ConnectionInfo;

/**
 * Событие транзакции - изменение JPObject
 */
public interface JPTransactionJPObjectEvent extends JPTransactionEvent {
  /**
   * Возвращает событие
   *
   * @return Событие
   */
  Event getEvent();

  /**
   * Идентификатор объекта
   *
   * @return Идентификатор объекта
   */
  Comparable getId();

  /**
   * Кодовое имя класса
   *
   * @return Кодовое имя класса
   */
  String getJpClassCode();

  /**
   * Источник события
   *
   * @return Источник события
   */
  ConnectionInfo getConnInfo();
}
