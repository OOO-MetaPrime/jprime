package mp.jprime.json.beans;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Описание ошибки
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonErrorDetail {
  private String code;
  private String message;

  public JsonErrorDetail() {
  }

  private JsonErrorDetail(String code, String message) {
    this.message = message;
    this.code = code;
  }

  public static JsonErrorDetail of(String code, String message) {
    return new JsonErrorDetail(code, message);
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
