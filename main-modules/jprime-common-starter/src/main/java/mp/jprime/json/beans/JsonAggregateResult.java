package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

/*
 * Модель данных ответа с агрегациями
 */
@JsonPropertyOrder({
    "classCode",
    "aggrs"
})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonAggregateResult {
  private String classCode;
  private Map<String, Object> aggrs;

  public JsonAggregateResult() {

  }

  private JsonAggregateResult(String classCode, Map<String, Object> aggrs) {
    this.classCode = classCode;
    this.aggrs = aggrs;
  }

  public String getClassCode() {
    return classCode;
  }

  public Map<String, Object> getAggrs() {
    return aggrs;
  }

  /**
   * Построитель JsonAggregateResult
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JsonAggregateResult
   */
  public static final class Builder {
    private String classCode;
    private Map<String, Object> aggrs = new HashMap<>();

    private Builder() {
    }

    /**
     * Кодовое имя класса
     *
     * @param classCode Кодовое имя класса
     * @return Builder
     */
    public Builder classCode(String classCode) {
      this.classCode = classCode;
      return this;
    }

    /**
     * Результаты агрегации
     *
     * @param aggrs Результаты агрегации
     * @return Builder
     */
    public Builder aggrs(Map<String, Object> aggrs) {
      if (aggrs != null) {
        this.aggrs.putAll(aggrs);
      }
      return this;
    }

    /**
     * Создаем JsonAggregateResult
     *
     * @return JsonAggregateResult
     */
    public JsonAggregateResult build() {
      return new JsonAggregateResult(classCode, aggrs);
    }
  }
}
