package mp.jprime.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.utils.annotations.JPUtilResultType;

/**
 * Тип результата - отсутствие реакции
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JPUtilResultType(code = JPUtilVoidOutParams.CODE)
public final class JPUtilVoidOutParams extends BaseJPUtilOutParams<Void> {
  public static final String CODE = "void";

  public static final JPUtilVoidOutParams EMPTY = JPUtilVoidOutParams.newBuilder().build();

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
