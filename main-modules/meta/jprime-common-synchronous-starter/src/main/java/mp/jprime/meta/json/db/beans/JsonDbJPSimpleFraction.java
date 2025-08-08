package mp.jprime.meta.json.db.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class JsonDbJPSimpleFraction {
  /**
   * Атрибут для хранения - Целая часть дроби
   */
  private String integerAttrCode;
  /**
   * Атрибут для хранения - Знаменатель дроби
   */
  private String denominatorAttrCode;

  public String getIntegerAttrCode() {
    return integerAttrCode;
  }

  public String getDenominatorAttrCode() {
    return denominatorAttrCode;
  }

  public JsonDbJPSimpleFraction() {

  }

  private JsonDbJPSimpleFraction(String integerAttrCode, String denominatorAttrCode) {
    this.integerAttrCode = integerAttrCode;
    this.denominatorAttrCode = denominatorAttrCode;
  }

  /**
   * Построитель JsonJPSimpleFraction
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JsonJPSimpleFraction
   */
  public static final class Builder {
    private String integerAttrCode;
    private String denominatorAttrCode;

    private Builder() {

    }

    public Builder integerAttrCode(String integerAttrCode) {
      this.integerAttrCode = integerAttrCode;
      return this;
    }

    public Builder denominatorAttrCode(String denominatorAttrCode) {
      this.denominatorAttrCode = denominatorAttrCode;
      return this;
    }

    public JsonDbJPSimpleFraction build() {
      return new JsonDbJPSimpleFraction(integerAttrCode, denominatorAttrCode);
    }
  }
}
