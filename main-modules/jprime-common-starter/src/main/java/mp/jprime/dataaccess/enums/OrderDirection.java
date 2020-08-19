package mp.jprime.dataaccess.enums;

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

  private String code;

  OrderDirection(String code) {
    this.code = code;
  }

  /**
   * Возвращает направление по коду
   *
   * @param code Код
   * @return Направление сортировки
   */
  public static OrderDirection getOrder(String code) {
    if (code == null) {
      return null;
    }
    for (OrderDirection v : OrderDirection.values()) {
      if (v.code.equalsIgnoreCase(code)) {
        return v;
      }
    }
    return null;
  }
}
