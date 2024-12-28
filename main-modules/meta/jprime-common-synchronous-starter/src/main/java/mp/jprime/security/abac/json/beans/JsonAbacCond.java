package mp.jprime.security.abac.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;

/**
 * Условие с массивом
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonAbacCond {
  private String cond;
  private Collection<String> values;

  public JsonAbacCond() {

  }

  private JsonAbacCond(String cond, Collection<String> values) {
    this.cond = cond;
    this.values = values;
  }

  /**
   * Условия
   *
   * @return Условия
   */
  public String getCond() {
    return cond;
  }

  /**
   * Значения
   *
   * @return Значения
   */
  public Collection<String> getValues() {
    return values;
  }


  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private String cond;
    private Collection<String> values;

    private Builder() {
    }

    public Builder cond(String cond) {
      this.cond = cond;
      return this;
    }

    public Builder values(Collection<String> values) {
      this.values = values;
      return this;
    }

    public JsonAbacCond build() {
      return new JsonAbacCond(cond, values);
    }
  }
}
