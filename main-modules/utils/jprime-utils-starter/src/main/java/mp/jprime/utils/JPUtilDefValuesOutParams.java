package mp.jprime.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.utils.annotations.JPUtilResultType;

/**
 * Базовая реализация для исходящих дефолтных значений
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JPUtilResultType(code = "inParamsDefValues")
public class JPUtilDefValuesOutParams extends BaseJPUtilOutParams<Object> {
  /**
   * Признак необходимости внесения изменений
   */
  private final boolean changeNeed;
  /**
   * Json объект со значениями по умолчанию
   */
  private final Object result;

  private JPUtilDefValuesOutParams(String description, String qName, boolean changeNeed, Object result) {
    super(description, qName, false, false);
    this.changeNeed = changeNeed;
    this.result = result;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public boolean isChangeNeed() {
    return changeNeed;
  }

  @Override
  public String getResultType() {
    return super.getResultType();
  }

  @Override
  public Object getResult() {
    return result;
  }

  public static final class Builder extends BaseJPUtilOutParams.Builder<Builder> {
    private boolean changeNeed;
    private Object result;

    public Builder() {
    }

    /**
     * Необходимость внесения данных
     *
     * @param changeNeed Признак, что надо изменить данные
     * @return Да/Нет
     */
    public Builder changeNeed(boolean changeNeed) {
      this.changeNeed = changeNeed;
      return this;
    }

    public Builder result(Object result) {
      this.result = result;
      return this;
    }

    @Override
    public JPUtilDefValuesOutParams build() {
      return new JPUtilDefValuesOutParams(description, qName, changeNeed, result);
    }
  }
}
