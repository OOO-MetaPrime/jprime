package mp.jprime.utils.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "utilCode",
    "modeCode",
    "title",
    "qName"
})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class JsonUtilModeLabel {
  private String utilCode;
  private String modeCode;
  private String title;
  private String qName;

  public JsonUtilModeLabel() {

  }

  private JsonUtilModeLabel(String utilCode, String modeCode, String title, String qName) {
    this.utilCode = utilCode;
    this.modeCode = modeCode;
    this.title = title;
    this.qName = qName;
  }

  public String getUtilCode() {
    return utilCode;
  }

  public String getModeCode() {
    return modeCode;
  }

  public String getTitle() {
    return title;
  }

  public String getqName() {
    return qName;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private String utilCode;
    private String modeCode;
    private String title;
    private String qName;

    private Builder() {
    }

    public JsonUtilModeLabel build() {
      return new JsonUtilModeLabel(utilCode, modeCode, title, qName);
    }

    public Builder utilCode(String utilCode) {
      this.utilCode = utilCode;
      return this;
    }

    public Builder modeCode(String modeCode) {
      this.modeCode = modeCode;
      return this;
    }

    public Builder title(String title) {
      this.title = title;
      return this;
    }

    public Builder qName(String qName) {
      this.qName = qName;
      return this;
    }
  }
}
