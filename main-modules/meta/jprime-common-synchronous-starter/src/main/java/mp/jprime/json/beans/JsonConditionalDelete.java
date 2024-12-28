package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Модель удаления объектов по условию
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonConditionalDelete extends JsonIdentityData {
  /**
   * Условие ограничения выборки
   */
  private JsonExpr filter;

  public JsonExpr getFilter() {
    return filter;
  }

  private JsonConditionalDelete(String classCode, JsonExpr filter) {
    super(null, classCode);
    this.filter = filter;
  }

  public static JsonConditionalDelete of(String classCode, JsonExpr filter) {
    return new JsonConditionalDelete(classCode, filter);
  }

  public void setFilter(JsonExpr filter) {
    this.filter = filter;
  }
}
