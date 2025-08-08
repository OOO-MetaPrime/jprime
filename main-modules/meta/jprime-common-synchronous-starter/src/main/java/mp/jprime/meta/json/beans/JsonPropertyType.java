package mp.jprime.meta.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import mp.jprime.beans.JPPropertyType;

/**
 * Тип свойства
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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
