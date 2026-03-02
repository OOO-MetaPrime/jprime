package mp.jprime.schedule.log;

/**
 * Типы служебных событий
 */
public enum Event implements mp.jprime.log.Event {
  /**
   * Запуск задачи
   */
  TASK_STARTED("task_started", true),
  /**
   * Завершение задачи
   */
  TASK_FINISHED("task_finished", true),
  /**
   * Завершение задачи с ошибкой
   */
  TASK_FINISHED_WITH_ERROR("task_finished_with_error", false);


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
