package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Признак (свойство)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JsonSign {
  /**
   * Код
   */
  private String code;
  /**
   * Тип
   */
  private String type;
  /**
   * Название
   */
  private String title;
  /**
   * Значение
   */
  private Object value;
  /**
   * Человеко-читаемое обозначение значения (заголовок)
   */
  private String valueTitle;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public String getValueTitle() {
    return valueTitle;
  }

  public void setValueTitle(String valueTitle) {
    this.valueTitle = valueTitle;
  }
}
