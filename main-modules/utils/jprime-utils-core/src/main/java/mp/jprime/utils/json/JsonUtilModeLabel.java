package mp.jprime.utils.json;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;
import java.util.Collections;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class JsonUtilModeLabel {
  private String utilCode;
  private String modeCode;
  private String title;
  private String qName;
  private boolean uni;
  private Collection<String> jpClasses;
  private Collection<String> jpClassTags;
  private String type;

  public JsonUtilModeLabel() {

  }

  private JsonUtilModeLabel(String utilCode, String modeCode, String title, String qName,
                            boolean uni, Collection<String> jpClasses, Collection<String> jpClassTags,
                            String type) {
    this.utilCode = utilCode;
    this.modeCode = modeCode;
    this.title = title;
    this.qName = qName;
    this.uni = uni;
    this.jpClasses = jpClasses != null ? Collections.unmodifiableCollection(jpClasses) : Collections.emptyList();
    this.jpClassTags = jpClassTags != null ? Collections.unmodifiableCollection(jpClassTags) : Collections.emptyList();
    this.type = type;
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

  public boolean isUni() {
    return uni;
  }

  public Collection<String> getJpClasses() {
    return jpClasses;
  }

  public Collection<String> getJpClassTags() {
    return jpClassTags;
  }

  public String getType() {
    return type;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private String utilCode;
    private String modeCode;
    private String title;
    private String qName;
    private boolean uni;
    private Collection<String> jpClasses;
    private Collection<String> jpClassTags;
    private String type;

    private Builder() {
    }

    public JsonUtilModeLabel build() {
      return new JsonUtilModeLabel(utilCode, modeCode, title, qName,
          uni, jpClasses, jpClassTags, type);
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

    public Builder uni(boolean uni) {
      this.uni = uni;
      return this;
    }

    public Builder jpClasses(Collection<String> jpClasses) {
      this.jpClasses = jpClasses;
      return this;
    }

    public Builder jpClassTags(Collection<String> jpClassTags) {
      this.jpClassTags = jpClassTags;
      return this;
    }

    public Builder type(String type) {
      this.type = type;
      return this;
    }
  }
}
