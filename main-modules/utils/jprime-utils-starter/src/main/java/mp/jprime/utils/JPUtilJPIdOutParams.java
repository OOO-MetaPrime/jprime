package mp.jprime.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.json.beans.JsonJPId;
import mp.jprime.utils.annotations.JPUtilResultType;

/**
 * Тип результата - идентификатор объекта
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JPUtilResultType(code = "jpId")
public final class JPUtilJPIdOutParams extends BaseJPUtilOutParams<JsonJPId> {
  private final JsonJPId jpId;

  private JPUtilJPIdOutParams(String description, String qName, boolean changeData, boolean deleteData, JsonJPId jpId) {
    super(description, qName, changeData, deleteData);
    this.jpId = jpId;
  }

  /**
   * Результата
   *
   * @return Результат
   */
  @Override
  public JsonJPId getResult() {
    return jpId;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public final static class Builder extends BaseJPUtilOutParams.Builder<Builder> {
    private JsonJPId jpId;

    private Builder() {
      super();
    }

    @Override
    public JPUtilJPIdOutParams build() {
      return new JPUtilJPIdOutParams(description, qName, changeData, deleteData, jpId);
    }

    public Builder jpId(JsonJPId jpId) {
      this.jpId = jpId;
      return this;
    }

    public Builder jpId(JPId jpId) {
      this.jpId = jpId == null ? null : JsonJPId.newBuilder()
          .classCode(jpId.getJpClass())
          .id(jpId.getId())
          .build();
      return this;
    }
  }
}
