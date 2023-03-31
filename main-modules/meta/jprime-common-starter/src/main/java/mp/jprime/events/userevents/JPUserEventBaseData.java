package mp.jprime.events.userevents;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Доп. данные пользовательского события
 */
public class JPUserEventBaseData implements JPUserEventData {
  private final Map<String, Object> addInfo = new HashMap<>();
  @JsonIgnore
  private final Map<String, Object> umAddInfo = Collections.unmodifiableMap(addInfo);

  protected JPUserEventBaseData(Map<String, Object> addInfo) {
    if (addInfo != null) {
      this.addInfo.putAll(addInfo);
    }
  }

  public Map<String, Object> getAddInfo() {
    return umAddInfo;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private final Map<String, Object> addInfo = new HashMap<>();

    private Builder() {
    }

    /**
     * Доп. значение
     */
    public Builder addInfo(String fileTitle, Object value) {
      this.addInfo.put(fileTitle, value);
      return this;
    }

    public JPUserEventData build() {
      return new JPUserEventBaseData(addInfo);
    }
  }
}
