package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonSubquery {
  private String attr;
  private String classCode;
  private JsonExpr filter;

  public String getAttr() {
    return attr;
  }

  public JsonSubquery attr(String attr) {
    this.attr = attr;
    return this;
  }

  public String getClassCode() {
    return classCode;
  }

  public JsonSubquery classCode(String classCode) {
    this.classCode = classCode;
    return this;
  }

  public JsonExpr getFilter() {
    return filter;
  }

  public JsonSubquery filter(JsonExpr filter) {
    this.filter = filter;
    return this;
  }
}
