package mp.jprime.utils.test.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.utils.JPUtilCustomOutParams;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Out extends JPUtilCustomOutParams {
  private final String value;

  private Out(String value) {
    super(null, null, false);
    this.value = value;
  }

  public String getValue() {
    return value;
  }


  /**
   * Создание нового объекта
   *
   * @param value Значение
   * @return Новый объект
   */
  public static Out newInstance(String value) {
    return new Out(value);
  }
}