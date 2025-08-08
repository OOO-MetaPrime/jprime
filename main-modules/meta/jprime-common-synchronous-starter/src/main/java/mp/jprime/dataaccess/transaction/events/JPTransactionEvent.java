package mp.jprime.dataaccess.transaction.events;

/**
 * Событие транзакции
 */
public interface JPTransactionEvent {
  /**
   * Возвращает код события
   *
   * @return Код
   */
  String getCode();
}
