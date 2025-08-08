package mp.jprime.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.json.beans.JsonDefValue;
import mp.jprime.lang.JPMap;
import mp.jprime.utils.annotations.JPUtilResultType;

/**
 * Тип результата - Создание объекта
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JPUtilResultType(code = "jpCreate")
public final class JPUtilJPCreateOutParams extends BaseJPUtilOutParams<JsonDefValue> {
  private final JsonDefValue defValue;

  private JPUtilJPCreateOutParams(String description, String qName, boolean changeData, boolean deleteData, JsonDefValue defValue) {
    super(description, qName, changeData, deleteData);
    this.defValue = defValue;
  }

  /**
   * Результата
   *
   * @return Результат
   */
  @Override
  public JsonDefValue getResult() {
    return defValue;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public final static class Builder extends BaseJPUtilOutParams.Builder<Builder> {
    private JsonDefValue defValue;

    private Builder() {
      super();
    }

    @Override
    public JPUtilJPCreateOutParams build() {
      return new JPUtilJPCreateOutParams(description, qName, changeData, deleteData, defValue);
    }

    public Builder create(String jpClassCode, JPMap data) {
      this.defValue = JsonDefValue.of(jpClassCode, data);
      return this;
    }
  }
}
