package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import mp.jprime.lang.JPStringRange;

/**
 * Диапазон string
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JsonStringRange extends JsonRange<String> {
  public JsonStringRange() {
  }

  public static JsonStringRange of(JPStringRange range) {
    JsonStringRange json = new JsonStringRange();
    json.setLower(range.lower());
    json.setUpper(range.upper());
    return json;
  }
}
