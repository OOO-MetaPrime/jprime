package mp.jprime.json.beans;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Описание ошибки
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonErrorDetail {
  private String message;
  private String code;

  public JsonErrorDetail() {
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
