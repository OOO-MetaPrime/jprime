package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Дипазон
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JsonBorderRange<T> extends JsonRange<T> {
  /**
   * Нижняя граница закрыта
   */
  private boolean closeLower = Boolean.TRUE;
  /**
   * Верхняя граница закрыта
   */
  private boolean closeUpper = Boolean.TRUE;

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
