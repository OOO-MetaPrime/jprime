package mp.jprime.log;

/**
 * Типы служебных событий
 */
public enum EventTest implements Event {
  TEST("test", true);

  private final String code;
  private final boolean success;

  EventTest(String code, boolean success) {
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