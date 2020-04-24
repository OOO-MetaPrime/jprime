package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

/*
 * Модель данных ответа со значениями по-умолчанию
 */
@JsonPropertyOrder({
    "classCode",
    "data"
})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonDefValueResult {
  private String classCode;
  private Map<String, Object> data;

  public JsonDefValueResult() {

  }

  private JsonDefValueResult(String classCode, Map<String, Object> data) {
    this.classCode = classCode;
    this.data = data;
  }

  public String getClassCode() {
    return classCode;
  }

  public Map<String, Object> getData() {
    return data;
  }

  /**
   * Построитель JsonDefValueResult
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JsonDefValueResult
   */
  public static final class Builder {
    private String classCode;
    private Map<String, Object> data = new HashMap<>();

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
     * Результаты значений по-умолчанию
     *
     * @param data Результаты значений по-умолчанию
     * @return Builder
     */
    public Builder data(Map<String, Object> data) {
      if (data != null) {
        this.data.putAll(data);
      }
      return this;
    }

    /**
     * Создаем JsonDefValueResult
     *
     * @return JsonDefValueResult
     */
    public JsonDefValueResult build() {
      return new JsonDefValueResult(classCode, data);
    }
  }
}
