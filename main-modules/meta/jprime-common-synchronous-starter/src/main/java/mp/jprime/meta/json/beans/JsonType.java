package mp.jprime.meta.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import mp.jprime.meta.beans.JPType;

/**
 * Типы атрибута
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class JsonType {
  private final String code;
  private final String title;

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
