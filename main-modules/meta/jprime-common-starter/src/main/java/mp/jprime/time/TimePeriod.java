package mp.jprime.time;

/**
 * Периоды времени
 */
public enum TimePeriod {
  DAY("day"),
  WEEK("week"),
  MONTH("month"),
  YEAR("year"),
  NONE("none");

  private final String code;

  TimePeriod(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  /**
   * Возвращает  период времени
   *
   * @param code Код
   * @return период времени
   */
  public static TimePeriod getPeriod(String code) {
    if (code == null) {
      return null;
    }
    for (TimePeriod v : TimePeriod.values()) {
      if (v.code.equalsIgnoreCase(code)) {
        return v;
      }
    }
    return null;
  }
}
