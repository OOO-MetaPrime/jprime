package mp.jprime.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Направление сортировки
 */
public enum JPOrderDirection {
  /**
   * По возрастанию
   */
  ASC("ASC"),
  /**
   * По убыванию
   */
  DESC("DESC");

  private static final Map<String, JPOrderDirection> BY_CODE = new HashMap<>();

  static {
    for (JPOrderDirection type : JPOrderDirection.values()) {
      BY_CODE.put(type.getCode().toLowerCase(), type);
    }
  }

  private final String code;

  JPOrderDirection(String code) {
    this.code = code;
  }

  /**
   * Код направления сортировки
   *
   * @return Код направления сортировки
   */
  public String getCode() {
    return code;
  }

  /**
   * Возвращает направление по коду
   *
   * @param code Код
   * @return Направление сортировки
   */
  public static JPOrderDirection getOrder(String code) {
    return code == null ? null : BY_CODE.get(code.toLowerCase());
  }
}
