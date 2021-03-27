package mp.jprime.utils.testutils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.utils.JPUtilCustomOutParams;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Out extends JPUtilCustomOutParams {
  private final String response;

  private Out(String response) {
    super(null, null, false);
    this.response = response;
  }

  public String getResponse() {
    return response;
  }

  /**
   * Создание нового объекта
   *
   * @param response Значение
   * @return Новый объект
   */
  public static Out newInstance(String response) {
    return new Out(response);
  }
}