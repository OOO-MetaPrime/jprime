package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/*
 * Модель данных дополнительных данных
 */
@JsonPropertyOrder({
    "code",
    "info"
})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonAddInfo {
  /**
   * Код
   */
  private String code;
  /**
   * Информация
   */
  private String info;

  public JsonAddInfo() {

  }


  private JsonAddInfo(String code, String info) {
    this.code = code;
    this.info = info;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  /**
   * Построитель JsonAddInfo
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JsonAddInfo
   */
  public static final class Builder {
    private String code;
    private String info;

    private Builder() {

    }

    /**
     * Создаем JsonAddInfo
     *
     * @return JsonAddInfo
     */
    public JsonAddInfo build() {
      return new JsonAddInfo(code, info);
    }

    /**
     * Код
     *
     * @param code Код
     * @return Builder
     */
    public Builder code(String code) {
      this.code = code;
      return this;
    }

    /**
     * Информация
     *
     * @param info Информация
     * @return Builder
     */
    public Builder info(String info) {
      this.info = info;
      return this;
    }
  }
}
