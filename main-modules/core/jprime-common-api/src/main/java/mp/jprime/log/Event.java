package mp.jprime.log;

/**
 * Интерфейс события
 */
public interface Event {
  /**
   * Код события
   *
   * @return Код события
   */
  String getCode();

  /**
   * Признак успешности
   *
   * @return Признак успешности
   */
  boolean isSuccess();
}
