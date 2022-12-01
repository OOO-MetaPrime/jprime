package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonUniqueValues {
  /**
   * Вложенные значения
   */
  private final Collection<JsonUniqueValue> values;

  private JsonUniqueValues(Collection<JsonUniqueValue> values) {
    this.values = values;
  }

  public static JsonUniqueValues newInstance(Collection<JsonUniqueValue> values) {
    return new JsonUniqueValues(values);
  }

  public Collection<JsonUniqueValue> getValues() {
    return values;
  }
}