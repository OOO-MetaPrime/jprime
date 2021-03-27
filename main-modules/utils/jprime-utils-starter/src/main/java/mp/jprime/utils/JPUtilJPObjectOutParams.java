package mp.jprime.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.json.beans.JsonJPObject;
import mp.jprime.utils.annotations.JPUtilResultType;

/**
 * Тип результата - объект
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JPUtilResultType(code = "jpObject")
public final class JPUtilJPObjectOutParams extends BaseJPUtilOutParams<JsonJPObject> {
  private final JsonJPObject jpObject;


  private JPUtilJPObjectOutParams(String description, String qName, boolean changeData, JsonJPObject jpObject) {
    super(description, qName, changeData);
    this.jpObject = jpObject;
  }

  /**
   * Результата
   *
   * @return Результат
   */
  @Override
  public JsonJPObject getResult() {
    return jpObject;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public final static class Builder extends BaseJPUtilOutParams.Builder<Builder> {
    private JsonJPObject jpObject;

    private Builder() {
      super();
    }

    @Override
    public JPUtilJPObjectOutParams build() {
      return new JPUtilJPObjectOutParams(description, qName, changeData, jpObject);
    }

    public Builder jpObject(JsonJPObject jpObject) {
      this.jpObject = jpObject;
      return this;
    }
  }
}
