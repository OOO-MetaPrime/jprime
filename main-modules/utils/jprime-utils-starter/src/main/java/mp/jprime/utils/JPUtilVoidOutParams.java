package mp.jprime.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.utils.annotations.JPUtilResultType;

/**
 * Тип результата - отсутствие реакции
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JPUtilResultType(code = "void")
public final class JPUtilVoidOutParams extends BaseJPUtilOutParams<Void> {

  private JPUtilVoidOutParams(boolean changeData) {
    super(null, null, changeData);
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

    public JPUtilVoidOutParams build() {
      return new JPUtilVoidOutParams(changeData);
    }

    public Builder changeData(boolean changeData) {
      this.changeData = changeData;
      return this;
    }
  }
}
