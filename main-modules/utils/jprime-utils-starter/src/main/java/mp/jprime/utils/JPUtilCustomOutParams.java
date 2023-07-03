package mp.jprime.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.utils.annotations.JPUtilResultType;

/**
 * Тип результата - произвольный
 */
@JPUtilResultType(code = "custom")
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class JPUtilCustomOutParams extends BaseJPUtilOutParams<Object> {
  protected JPUtilCustomOutParams(String description, String qName, boolean changeData, boolean deleteData) {
    super(description, qName, changeData, deleteData);
  }

  protected JPUtilCustomOutParams(String description, String qName, boolean changeData) {
    super(description, qName, changeData);
  }

  /**
   * Результата
   *
   * @return Результат
   */
  @Override
  public Object getResult() {
    return null;
  }

  public abstract static class Builder<T extends BaseJPUtilOutParams.Builder> extends BaseJPUtilOutParams.Builder<T> {
    protected Builder() {
      super();
    }
  }
}
