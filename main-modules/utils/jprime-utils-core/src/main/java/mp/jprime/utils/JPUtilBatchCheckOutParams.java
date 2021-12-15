package mp.jprime.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import mp.jprime.utils.annotations.JPUtilResultType;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Тип, возвращаемый методом batchCheck()
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JPUtilResultType(code = "batchCheck")
public class JPUtilBatchCheckOutParams implements JPUtilOutParams<Collection<JPUtilBatchCheckOutParams.CheckResult>> {

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class CheckResult {
    private final String objectClassCode;
    private final String objectId;
    private final String utilCode;
    private final JPUtilCheckOutParams result;

    @JsonCreator
    public CheckResult(@JsonProperty("objectClassCode") String objectClassCode,
                       @JsonProperty("objectId") String objectId,
                       @JsonProperty("utilCode") String utilCode,
                       @JsonProperty("result") JPUtilCheckOutParams result) {
      this.objectClassCode = objectClassCode;
      this.objectId = objectId;
      this.utilCode = utilCode;
      this.result = result;
    }

    /**
     * Кодовое имя метаописания класса
     *
     * @return Кодовое имя метаописания класса
     */
    public String getObjectClassCode() {
      return objectClassCode;
    }

    /**
     * Идентификатор объекта
     *
     * @return Идентификатор объекта
     */
    public String getObjectId() {
      return objectId;
    }

    /**
     * Код утилиты
     *
     * @return Код утилиты
     */
    public String getUtilCode() {
      return utilCode;
    }

    /**
     * Результат проверки
     *
     * @return Результат проверки
     */
    public JPUtilCheckOutParams getResult() {
      return result;
    }
  }

  /**
   * Результаты проверки
   */
  private final Collection<CheckResult> result;

  @JsonCreator
  public JPUtilBatchCheckOutParams(@JsonProperty("result") Collection<CheckResult> result) {
    this.result = result;
  }

  @Override
  public String getDescription() {
    return null;
  }

  @Override
  public String getQName() {
    return null;
  }

  @Override
  public boolean isChangeData() {
    return false;
  }

  /**
   * Результат
   *
   * @return Результат
   */
  @Override
  public Collection<CheckResult> getResult() {
    return result;
  }


  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private Collection<CheckResult> results = new CopyOnWriteArrayList<>();

    private Builder() {
      super();
    }


    public Builder add(String objectClassCode, String objectId, String utilCode, JPUtilCheckOutParams result) {
      if (objectId != null && utilCode != null) {
        this.results.add(
            new CheckResult(objectClassCode, objectId, utilCode, result)
        );
      }
      return this;
    }

    public Builder addAll(Collection<CheckResult> results) {
      if (results != null) {
        this.results.addAll(results);
      }
      return this;
    }

    public JPUtilBatchCheckOutParams build() {
      return new JPUtilBatchCheckOutParams(results);
    }
  }
}
