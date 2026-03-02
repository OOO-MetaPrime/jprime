package mp.jprime.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.utils.annotations.JPUtilResultType;

/**
 * Тип результата - строка
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JPUtilResultType(code = JPUtilMessageOutParams.CODE)
public final class JPUtilMessageOutParams extends BaseJPUtilOutParams<Void> {
  public static final String CODE = "message";

  private JPUtilMessageOutParams(String description, String qName, boolean changeData, boolean deleteData) {
    super(description, qName, changeData, deleteData);
  }

  /**
   * Результата
   *
   * @return Результат
   */
  @Override
  public Void getResult() {
    return null;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public final static class Builder extends BaseJPUtilOutParams.Builder<Builder> {
    private Builder() {
      super();
    }

    @Override
    public JPUtilMessageOutParams build() {
      return new JPUtilMessageOutParams(description, qName, changeData, deleteData);
    }
  }
}
