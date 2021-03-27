package mp.jprime.utils.log;

/**
 * Типы служебных событий
 */
public enum Event implements mp.jprime.log.Event {
  /**
   * Вызов утилиты
   */
  UTIL_RUN("util_run", true);


  private String code;
  private boolean success;

  Event(String code, boolean success) {
    this.code = code;
    this.success = success;
  }

  /**
   * Код события
   *
   * @return Код события
   */
  @Override
  public String getCode() {
    return code;
  }

  /**
   * Признак успешности
   *
   * @return Признак успешности
   */
  @Override
  public boolean isSuccess() {
    return success;
  }
}
