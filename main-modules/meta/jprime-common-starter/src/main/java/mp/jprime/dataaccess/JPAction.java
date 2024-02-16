package mp.jprime.dataaccess;

import java.util.HashMap;
import java.util.Map;

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

  private static final Map<String, JPAction> BY_CODE = new HashMap<>();

  static {
    for (JPAction type : JPAction.values()) {
      BY_CODE.put(type.getCode().toLowerCase(), type);
    }
  }

  private final String code;

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
    return code == null ? null : BY_CODE.get(code.toLowerCase());
  }
}
