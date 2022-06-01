package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

/**
 * Модель ответа на пополнение значений
 */
@JsonPropertyOrder({
    "id",
    "classCode",
    "data"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonApplyValueResult {
  private Object id = null;
  private String classCode = null;
  private Map<String, Object> data = new HashMap<>();

  public JsonApplyValueResult() {

  }

  private JsonApplyValueResult(Object id, String classCode, Map<String, Object> data) {
    this.id = id;
    this.classCode = classCode;
    this.data = data;
  }

  public Object getId() {
    return id;
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
    private Object id = null;
    private String classCode;
    private Map<String, Object> data = new HashMap<>();

    private Builder() {
    }

    /**
     * Идентификатор объкта
     *
     * @param id Идентификатор объкта
     * @return Builder
     */
    public Builder id(Object id) {
      this.id = id;
      return this;
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
     * Результаты значений по умолчанию
     *
     * @param data Результаты значений по умолчанию
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
    public JsonApplyValueResult build() {
      return new JsonApplyValueResult(id, classCode, data);
    }
  }
}
