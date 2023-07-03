package mp.jprime.utils.json;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class JsonUtilEnum {
  private Object value;
  private String description;
  private String qName;

  public JsonUtilEnum() {

  }

  private JsonUtilEnum(Object value, String description, String qName) {
    this.value = value;
    this.description = description;
    this.qName = qName;
  }

  public Object getValue() {
    return value;
  }

  public String getDescription() {
    return description;
  }

  public String getqName() {
    return qName;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private Object value;
    private String description;
    private String qName;


    private Builder() {
    }

    public JsonUtilEnum build() {
      return new JsonUtilEnum(value, description, qName);
    }

    public Builder value(Object value) {
      this.value = value;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder qName(String qName) {
      this.qName = qName;
      return this;
    }
  }
}
