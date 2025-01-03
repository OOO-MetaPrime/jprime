package mp.jprime.utils.log;

/**
 * Типы служебных событий
 */
public enum Event implements mp.jprime.log.Event {
  /**
   * Вызов утилиты
   */
  UTIL_RUN("util_run", true);

  private final String code;
  private final boolean success;

  Event(String code, boolean success) {
    this.code = code;
    this.success = success;
  }


  @Override
  public String getCode() {
    return code;
  }

  @Override
  public boolean isSuccess() {
    return success;
  }
}
