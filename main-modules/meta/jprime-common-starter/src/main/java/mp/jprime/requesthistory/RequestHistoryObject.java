package mp.jprime.requesthistory;

/**
 * Описание объекта истории
 */
public interface RequestHistoryObject {
  /**
   * Идентификатор
   *
   * @return Идентификатор
   */
  Object getId();

  /**
   * Код класса
   *
   * @return Код класса
   */
  String getClassCode();

  /**
   * Тело истории
   *
   * @return Тело истории
   */
  Object getBody();
}
