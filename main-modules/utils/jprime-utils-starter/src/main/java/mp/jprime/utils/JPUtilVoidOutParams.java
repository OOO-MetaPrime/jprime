package mp.jprime.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.utils.annotations.JPUtilResultType;

/**
 * Тип результата - отсутствие реакции
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JPUtilResultType(code = "void")
public final class JPUtilVoidOutParams extends BaseJPUtilOutParams<Void> {

  private JPUtilVoidOutParams(boolean changeData, boolean deleteData) {
    super(null, null, changeData, deleteData);
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

  public final static class Builder {
    private Builder() {
      super();
    }

    private boolean changeData;
    private boolean deleteData;

    public JPUtilVoidOutParams build() {
      return new JPUtilVoidOutParams(changeData, deleteData);
    }

    public Builder changeData(boolean changeData) {
      this.changeData = changeData;
      return this;
    }

    public Builder deleteData(boolean deleteData) {
      this.deleteData = deleteData;
      return this;
    }
  }
}
