package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonContainsKVP {
  private String key;
  private String value;

  public JsonContainsKVP() {
  }

  private JsonContainsKVP(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public static JsonContainsKVP from(String key, String value) {
    return new JsonContainsKVP(key, value);
  }
}
