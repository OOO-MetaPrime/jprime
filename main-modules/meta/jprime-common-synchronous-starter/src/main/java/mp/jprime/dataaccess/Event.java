package mp.jprime.dataaccess;

/**
 * Типы служебных событий
 */
public enum Event implements mp.jprime.log.Event {
  /**
   * Успешное создание
   */
  CREATE_SUCCESS("create_success", true),
  /**
   * Ошибка создания
   */
  CREATE_ERROR("create_error", false),
  /**
   * Успешное обновление
   */
  UPDATE_SUCCESS("update_success", true),
  /**
   * Ошибка обновления
   */
  UPDATE_ERROR("update_error", false),
  /**
   * Успешное удаление
   */
  DELETE_SUCCESS("delete_success", true),
  /**
   * Ошибка удаления
   */
  DELETE_ERROR("delete_error", false);


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