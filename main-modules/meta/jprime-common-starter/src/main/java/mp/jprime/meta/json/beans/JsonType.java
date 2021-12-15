package mp.jprime.meta.json.beans;

import mp.jprime.meta.beans.JPType;

/**
 * Типы атрибута
 */
public final class JsonType {
  private String code;
  private String title;

  private JsonType(String code, String title) {
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
  public static JsonType from(JPType type) {
    return new JsonType(type.getCode(), type.getTitle());
  }
}
