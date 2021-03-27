package mp.jprime.utils.test.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.utils.BaseJPUtilInParams;

@JsonIgnoreProperties(ignoreUnknown = true)
public class In extends BaseJPUtilInParams {
  private final String value;

  public String getValue() {
    return value;
  }

  private In(String value) {
    this.value = value;
  }

  public static In newInstance(String value) {
    return new In(value);
  }
}
