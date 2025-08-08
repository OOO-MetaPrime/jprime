package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;

/**
 * Результат валидации параметров утилиты
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonValidateResult {
  /**
   * Результат проверки параметров
   */
  private final boolean valid;
  /**
   * Список ошибок
   */
  private final Collection<JsonErrorDetail> details;

  public static JsonValidateResult valid() {
    return JsonValidateResult.newBuilder()
        .valid(true)
        .build();
  }

  public static JsonValidateResult nonValid(Collection<JsonErrorDetail> errorsMsg) {
    return JsonValidateResult.newBuilder()
        .valid(false)
        .details(errorsMsg)
        .build();
  }

  private JsonValidateResult(Builder builder) {
    valid = builder.valid;
    details = builder.details;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public boolean isValid() {
    return valid;
  }

  public Collection<JsonErrorDetail> getDetails() {
    return details;
  }


  public static final class Builder {
    private boolean valid;
    private Collection<JsonErrorDetail> details;

    private Builder() {
    }

    public Builder valid(boolean valid) {
      this.valid = valid;
      return this;
    }

    public Builder details(Collection<JsonErrorDetail> details) {
      this.details = details;
      return this;
    }

    public JsonValidateResult build() {
      return new JsonValidateResult(this);
    }
  }
}
