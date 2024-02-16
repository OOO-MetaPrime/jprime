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
  /**
   * Кодовое имя класса, на который ссылается
   */
  private String refJpClass;
  /**
   * Кодовое имя атрибута ссылочного класса
   */
  private String refJpAttr;

  private JsonSign(String code, String type, String title, Object value, String valueTitle,
                   String refJpClass, String refJpAttr) {
    this.code = code;
    this.type = type;
    this.title = title;
    this.value = value;
    this.valueTitle = valueTitle;
    this.refJpClass = refJpClass;
    this.refJpAttr = refJpAttr;
  }

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

  public String getRefJpClass() {
    return refJpClass;
  }

  public void setRefJpClass(String refJpClass) {
    this.refJpClass = refJpClass;
  }

  public String getRefJpAttr() {
    return refJpAttr;
  }

  public void setRefJpAttr(String refJpAttr) {
    this.refJpAttr = refJpAttr;
  }

  public static JsonSign of(String code, String type, String title, Object value) {
    return of(code, type, title, value, null);
  }

  public static JsonSign of(String code, String type, String title, Object value, String valueTitle) {
    return of(code, type, title, value, valueTitle, null, null);
  }

  public static JsonSign of(String code, String type, String title, Object value, String valueTitle, String refJpClass, String refJpAttr) {
    return new JsonSign(code, type, title, value, valueTitle, refJpClass, refJpAttr);
  }
}
