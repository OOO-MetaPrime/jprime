package mp.jprime.meta.log;

/**
 * События меты
 */
public enum Event implements mp.jprime.log.Event {
  /**
   * Идентиифкатор не найден
   */
  PRIMARY_KEY_NOT_FOUND("jpClass.primaryKey.notFound", false);

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
