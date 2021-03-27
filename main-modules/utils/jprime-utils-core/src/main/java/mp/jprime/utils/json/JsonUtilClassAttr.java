package mp.jprime.utils.json;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Путь атрибута
 */
@JsonPropertyOrder({
    "jpClass",
    "jpAttr"
})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class JsonUtilClassAttr {
  /**
   * Кодовое имя класса
   */
  private String jpClass;
  /**
   * Кодовое имя атрибута
   */
  private String jpAttr;

  public String getJpClass() {
    return jpClass;
  }

  public String getJpAttr() {
    return jpAttr;
  }

  public JsonUtilClassAttr() {

  }

  private JsonUtilClassAttr(String jpClass, String jpAttr) {
    this.jpClass = jpClass;
    this.jpAttr = jpAttr;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    /**
     * Кодовое имя класса
     */
    private String jpClass;
    /**
     * Кодовое имя атрибута
     */
    private String jpAttr;

    private Builder() {
    }

    public JsonUtilClassAttr build() {
      return new JsonUtilClassAttr(jpClass, jpAttr);
    }

    public Builder jpClass(String jpClass) {
      this.jpClass = jpClass;
      return this;
    }

    public Builder jpAttr(String jpAttr) {
      this.jpAttr = jpAttr;
      return this;
    }
  }
}
