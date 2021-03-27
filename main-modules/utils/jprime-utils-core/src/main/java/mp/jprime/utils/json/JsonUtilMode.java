package mp.jprime.utils.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Collection;
import java.util.Collections;

@JsonPropertyOrder({
    "utilCode",
    "modeCode",
    "code",
    "title",
    "qName",
    "confirmMessage",
    "jpClasses",
    "type",
    "jpAttrs",
    "inParams",
    "resultType",
    "outCustomParams"
})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class JsonUtilMode {
  private String utilCode;
  private String modeCode;
  private String title;
  private String qName;
  private String confirmMessage;
  private Collection<String> jpClasses;
  private String type;
  private Collection<JsonUtilClassAttr> jpAttrs;
  private Collection<JsonUtilParam> inParams;
  private String resultType;
  private Collection<JsonUtilParam> outCustomParams;

  public JsonUtilMode() {

  }

  private JsonUtilMode(String utilCode, String modeCode, String title, String qName, String confirmMessage,
                       Collection<String> jpClasses, String type, Collection<JsonUtilClassAttr> jpAttrs,
                       Collection<JsonUtilParam> inParams,
                       String resultType, Collection<JsonUtilParam> outCustomParams) {
    this.utilCode = utilCode;
    this.modeCode = modeCode;
    this.title = title;
    this.qName = qName;
    this.confirmMessage = confirmMessage;
    this.jpClasses = Collections.unmodifiableCollection(jpClasses != null ? jpClasses : Collections.emptyList());
    this.type = type;
    this.jpAttrs = Collections.unmodifiableCollection(jpAttrs != null ? jpAttrs : Collections.emptyList());
    this.inParams = Collections.unmodifiableCollection(inParams != null ? inParams : Collections.emptyList());
    this.resultType = resultType;
    this.outCustomParams = Collections.unmodifiableCollection(outCustomParams != null ? outCustomParams : Collections.emptyList());
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

  public String getConfirmMessage() {
    return confirmMessage;
  }

  public Collection<String> getJpClasses() {
    return jpClasses;
  }

  public String getType() {
    return type;
  }

  public Collection<JsonUtilClassAttr> getJpAttrs() {
    return jpAttrs;
  }

  /**
   * Описание входных параметров
   *
   * @return Описание входных параметров
   */
  public Collection<JsonUtilParam> getInParams() {
    return inParams;
  }

  /**
   * Тип результата
   *
   * @return Тип результата
   */
  public String getResultType() {
    return resultType;
  }

  /**
   * Описание итоговых параметров
   *
   * @return Описание итоговых параметров
   */
  public Collection<JsonUtilParam> getOutCustomParams() {
    return outCustomParams;
  }


  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private String utilCode;
    private String modeCode;
    private String title;
    private String qName;
    private String confirmMessage;
    private Collection<String> jpClasses;
    private String type;
    private Collection<JsonUtilClassAttr> jpAttrs;
    private Collection<JsonUtilParam> inParams;
    private String resultType;
    private Collection<JsonUtilParam> outCustomParams;

    private Builder() {
    }

    public JsonUtilMode build() {
      return new JsonUtilMode(utilCode, modeCode, title, qName, confirmMessage,
          jpClasses, type, jpAttrs, inParams, resultType, outCustomParams);
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

    public Builder confirmMessage(String confirmMessage) {
      this.confirmMessage = confirmMessage;
      return this;
    }

    public Builder jpClasses(Collection<String> jpClasses) {
      this.jpClasses = jpClasses;
      return this;
    }

    public Builder type(String type) {
      this.type = type;
      return this;
    }

    public Builder jpAttrs(Collection<JsonUtilClassAttr> jpAttrs) {
      this.jpAttrs = jpAttrs;
      return this;
    }

    public Builder inParams(Collection<JsonUtilParam> inParams) {
      this.inParams = inParams;
      return this;
    }

    public Builder resultType(String resultType) {
      this.resultType = resultType;
      return this;
    }

    public Builder outCustomParams(Collection<JsonUtilParam> outCustomParams) {
      this.outCustomParams = outCustomParams;
      return this;
    }
  }
}
