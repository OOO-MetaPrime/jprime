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
  /**
   * Нижняя граница закрыта
   */
  private boolean closeLower = Boolean.TRUE;
  /**
   * Верхняя граница закрыта
   */
  private boolean closeUpper = Boolean.TRUE;

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

  public boolean isCloseLower() {
    return closeLower;
  }

  public void setCloseLower(boolean closeLower) {
    this.closeLower = closeLower;
  }

  public boolean isCloseUpper() {
    return closeUpper;
  }

  public void setCloseUpper(boolean closeUpper) {
    this.closeUpper = closeUpper;
  }
}
