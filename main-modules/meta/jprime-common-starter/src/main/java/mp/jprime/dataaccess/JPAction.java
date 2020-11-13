package mp.jprime.dataaccess;

/**
 * Действия над объектами
 */
public enum JPAction {
  /**
   * Чтение
   */
  READ("read"),
  /**
   * Создание
   */
  CREATE("create"),
  /**
   * Обновление
   */
  UPDATE("update"),
  /**
   * Удаление
   */
  DELETE("delete");


  private String code;

  JPAction(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  @Override
  public String toString() {
    return "JPAction{" +
        "code='" + code + '\'' +
        '}';
  }

  /**
   * Возвращает действие
   *
   * @param code Код
   * @return действие
   */
  public static JPAction getType(String code) {
    if (code == null) {
      return null;
    }
    for (JPAction v : JPAction.values()) {
      if (v.code.equalsIgnoreCase(code)) {
        return v;
      }
    }
    return null;
  }
}
