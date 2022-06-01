package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Дипазон
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JsonRange<T> {
  /**
   * Нижняя граница
   */
  private T lower;
  /**
   * Верхняя граница
   */
  private T upper;

  public T getLower() {
    return lower;
  }

  public void setLower(T lower) {
    this.lower = lower;
  }

  public T getUpper() {
    return upper;
  }

  public void setUpper(T upper) {
    this.upper = upper;
  }
}
