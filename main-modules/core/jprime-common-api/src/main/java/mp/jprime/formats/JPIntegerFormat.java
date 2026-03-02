package mp.jprime.formats;

import java.util.HashMap;
import java.util.Map;

/**
 * Тип формата целочисленного
 */
public enum JPIntegerFormat {
  NONE("none", "Не определен"),
  YEAR("year", "Год");

  private static final Map<String, JPIntegerFormat> BY_CODE = new HashMap<>();

  static {
    for (JPIntegerFormat format : JPIntegerFormat.values()) {
      BY_CODE.put(format.getCode().toLowerCase(), format);
    }
  }

  /**
   * Код формата целочисленного
   */
  private final String code;

  /**
   * Название формата целочисленного
   */
  private final String name;

  JPIntegerFormat(String code, String name) {
    this.code = code;
    this.name = name;
  }

  /**
   * Возвращает код формата целочисленного
   *
   * @return Код формата целочисленного
   */
  public String getCode() {
    return code;
  }

  /**
   * Возвращает название формата целочисленного
   *
   * @return Название формата целочисленного
   */
  public String getName() {
    return name;
  }

  /**
   * Возвращает код формата целочисленного
   *
   * @return Код
   */
  public static String getCode(JPIntegerFormat format) {
    return format == null ? null : format.getCode();
  }

  /**
   * Возвращает тип формата целочисленного
   *
   * @param code Код
   * @return Тип формата целочисленного
   */
  public static JPIntegerFormat getType(String code) {
    return code == null ? null : BY_CODE.get(code.toLowerCase());
  }
}
