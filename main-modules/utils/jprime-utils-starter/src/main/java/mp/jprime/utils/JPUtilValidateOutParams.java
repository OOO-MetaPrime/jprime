package mp.jprime.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.json.beans.JsonErrorDetail;
import mp.jprime.json.beans.JsonValidateResult;
import mp.jprime.utils.annotations.JPUtilResultType;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;

/**
 * Тип, возвращаемый методом validate()
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JPUtilResultType(code = "validate")
public class JPUtilValidateOutParams extends BaseJPUtilOutParams<JsonValidateResult> {
  private final JsonValidateResult result;

  public static JPUtilValidateOutParams valid() {
    return JPUtilValidateOutParams.newBuilder()
        .result(JsonValidateResult.valid())
        .build();
  }

  public static JPUtilValidateOutParams nonValid(Collection<JsonErrorDetail> errorsMsg) {
    return JPUtilValidateOutParams.newBuilder()
        .result(JsonValidateResult.nonValid(errorsMsg))
        .build();
  }

  public static JPUtilValidateOutParams defineValid(Collection<JsonErrorDetail> errorsMsg) {
    if (CollectionUtils.isEmpty(errorsMsg)) {
      return JPUtilValidateOutParams.valid();
    }
    return JPUtilValidateOutParams.nonValid(errorsMsg);
  }

  private JPUtilValidateOutParams(String description, String qName, boolean changeData, JsonValidateResult result) {
    super(description, qName, changeData);
    this.result = result;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  @Override
  public JsonValidateResult getResult() {
    return result;
  }


  public static final class Builder extends BaseJPUtilOutParams.Builder<Builder> {
    private JsonValidateResult result;

    private Builder() {
    }

    public Builder result(JsonValidateResult result) {
      this.result = result;
      return this;
    }

    public JPUtilValidateOutParams build() {
      return new JPUtilValidateOutParams(description, qName, false, result);
    }
  }
}
