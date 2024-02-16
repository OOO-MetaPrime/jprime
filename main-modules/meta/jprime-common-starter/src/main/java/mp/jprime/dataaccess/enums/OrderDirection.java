package mp.jprime.dataaccess.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Направление сортировки
 */
public enum OrderDirection {
  /**
   * По возрастанию
   */
  ASC("ASC"),
  /**
   * По убыванию
   */
  DESC("DESC");

  private static final Map<String, OrderDirection> BY_CODE = new HashMap<>();

  static {
    for (OrderDirection type : OrderDirection.values()) {
      BY_CODE.put(type.getCode().toLowerCase(), type);
    }
  }

  private final String code;

  OrderDirection(String code) {
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
  public static OrderDirection getOrder(String code) {
    return code == null ? null : BY_CODE.get(code.toLowerCase());
  }
}
