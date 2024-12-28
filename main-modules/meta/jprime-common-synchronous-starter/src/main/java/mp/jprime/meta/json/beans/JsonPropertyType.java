package mp.jprime.meta.json.beans;

import mp.jprime.beans.JPPropertyType;

/**
 * Тип свойства
 */
public final class JsonPropertyType {
  private final String code;
  private final String title;

  private JsonPropertyType(String code, String title) {
    this.code = code;
    this.title = title;
  }

  public String getCode() {
    return code;
  }

  public String getTitle() {
    return title;
  }

  /**
   * Создаение JsonType
   *
   * @param type JPType
   * @return JsonType
   */
  public static JsonPropertyType from(JPPropertyType type) {
    return new JsonPropertyType(type.getCode(), type.getTitle());
  }
}
