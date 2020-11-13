package mp.jprime.meta.json.beans;

/**
 * Типы атрибута
 */
public final class JsonType {
  private String code;
  private String title;

  public JsonType(String code, String title) {
    this.code = code;
    this.title = title;
  }

  public String getCode() {
    return code;
  }

  public String getTitle() {
    return title;
  }
}
