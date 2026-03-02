package mp.jprime.schedule;

import java.util.HashMap;
import java.util.Map;

/**
 * Типы настроек крона
 */
public enum JpScheduleType {
  /**
   * Всегда
   */
  EVERY(1),
  /**
   * Каждые /N
   */
  EVERY_N(2),
  /**
   * Явно указанные значения 1,2,3
   */
  SPECIFIC(3);

  private final int code;

  JpScheduleType(int code) {
    this.code = code;
  }

  private static final Map<Integer, JpScheduleType> BY_CODE = new HashMap<>();

  static {
    for (JpScheduleType alphabet : JpScheduleType.values()) {
      BY_CODE.put(alphabet.getCode(), alphabet);
    }
  }

  public static JpScheduleType getType(Integer code) {
    return code == null ? null : BY_CODE.get(code);
  }

  /**
   * Получить код настройки
   *
   * @return Код настройки
   */
  public int getCode() {
    return code;
  }
}
