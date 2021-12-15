package mp.jprime.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import mp.jprime.utils.annotations.JPUtilResultType;

/**
 * Тип, возвращаемый методом check()
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JPUtilResultType(code = "check")
public class JPUtilCheckOutParams extends BaseJPUtilOutParams<Void> {
  /**
   * Сообщение подтверждения
   */
  private final String confirm;
  /**
   * Признак запрета доступа
   */
  private final boolean denied;

  @JsonCreator
  public JPUtilCheckOutParams(@JsonProperty("description") String description,
                              @JsonProperty("qName") String qName,
                              @JsonProperty("changeData") boolean changeData,
                              @JsonProperty("confirm") String confirm,
                              @JsonProperty("denied") boolean denied)  {
    super(description, qName, changeData);
    this.confirm = confirm;
    this.denied = denied;
  }

  @JsonProperty("confirm")
  public String getConfirm() {
    return confirm;
  }

  public boolean isDenied() {
    return denied;
  }

  /**
   * Результат
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

  public static final class Builder extends BaseJPUtilOutParams.Builder<Builder> {
    private String confirm;
    private boolean denied;

    private Builder() {
      super();
    }

    public Builder confirm(String confirm) {
      this.confirm = confirm;
      return this;
    }

    public Builder denied(boolean denied) {
      this.denied = denied;
      return this;
    }

    @Override
    public JPUtilCheckOutParams build() {
      return new JPUtilCheckOutParams(description, qName, changeData, confirm, denied);
    }
  }
}
