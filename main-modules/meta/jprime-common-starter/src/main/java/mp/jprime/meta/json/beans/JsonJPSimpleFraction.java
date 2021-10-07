package mp.jprime.meta.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class JsonJPSimpleFraction {
  /**
   * Атрибут для хранения - Целая часть дроби
   */
  private String integerAttr;
  /**
   * Атрибут для хранения - Знаменатель дроби
   */
  private String denominatorAttr;

  public String getIntegerAttr() {
    return integerAttr;
  }

  public String getDenominatorAttr() {
    return denominatorAttr;
  }

  public JsonJPSimpleFraction() {

  }

  private JsonJPSimpleFraction(String integerAttr, String denominatorAttr) {
    this.integerAttr = integerAttr;
    this.denominatorAttr = denominatorAttr;
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
    private String integerAttr;
    private String denominatorAttr;

    private Builder() {

    }

    public Builder integerAttr(String integerAttr) {
      this.integerAttr = integerAttr;
      return this;
    }

    public Builder denominatorAttr(String denominatorAttr) {
      this.denominatorAttr = denominatorAttr;
      return this;
    }

    public JsonJPSimpleFraction build() {
      return new JsonJPSimpleFraction(integerAttr, denominatorAttr);
    }
  }
}
