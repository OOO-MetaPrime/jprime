package mp.jprime.utils;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Все входящие параметры в виде Map
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class MapInParams extends BaseJPUtilInParams {
  private final Map<String, Object> otherFields = new LinkedHashMap<>();

  @JsonAnySetter
  void setOtherField(String key, Object value) {
    otherFields.put(key, value);
  }

  @JsonAnyGetter
  public Map<String, Object> getOtherFields() {
    return otherFields;
  }
}
