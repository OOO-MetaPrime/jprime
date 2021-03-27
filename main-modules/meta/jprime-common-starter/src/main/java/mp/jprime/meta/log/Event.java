package mp.jprime.meta.log;

/**
 * События меты
 */
public enum Event implements mp.jprime.log.Event {
  /**
   * Идентиифкатор не найден
   */
  PRIMARY_KEY_NOT_FOUND("jpClass.primaryKey.notFound", false);

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
