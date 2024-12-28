package mp.jprime.time;

import java.util.HashMap;
import java.util.Map;

/**
 * Периоды времени
 */
public enum TimePeriod {
  DAY("day"),
  WEEK("week"),
  MONTH("month"),
  YEAR("year"),
  NONE("none");

  private static final Map<String, TimePeriod> BY_CODE = new HashMap<>();

  static {
    for (TimePeriod type : TimePeriod.values()) {
      BY_CODE.put(type.getCode().toLowerCase(), type);
    }
  }

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
    return code == null ? null : BY_CODE.get(code.toLowerCase());
  }
}
