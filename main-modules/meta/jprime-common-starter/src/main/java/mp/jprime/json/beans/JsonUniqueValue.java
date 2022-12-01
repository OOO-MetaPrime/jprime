package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonUniqueValue {
  /**
   * Идентификатор записи
   */
  private final Object id;
  /**
   * Атрибут
   */
  private final String attr;
  /**
   * Значение атрибута
   */
  private final Object value;
  /**
   * Вложенные значения
   */
  private final Collection<JsonUniqueValue> subValues;

  public JsonUniqueValue(Object id, String attr, Object value, Collection<JsonUniqueValue> subValues) {
    this.id = id;
    this.attr = attr;
    this.value = value;
    this.subValues = subValues;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public Object getId() {
    return id;
  }

  public String getAttr() {
    return attr;
  }

  public Object getValue() {
    return value;
  }

  public Collection<JsonUniqueValue> getSubValues() {
    return subValues;
  }

  public static final class Builder {
    /**
     * Идентификатор записи
     */
    private Object id;
    /**
     * Атрибут
     */
    private String attr;
    /**
     * Значение атрибута
     */
    private Object value;
    /**
     * Вложенные значения
     */
    private Collection<JsonUniqueValue> subValues;

    public Builder id(Object id) {
      this.id = id;
      return this;
    }

    public Builder attr(String attr) {
      this.attr = attr;
      return this;
    }

    public Builder value(Object value) {
      this.value = value;
      return this;
    }


    public Builder subValues(Collection<JsonUniqueValue> subValues) {
      this.subValues = subValues;
      return this;
    }

    public JsonUniqueValue build() {
      return new JsonUniqueValue(id, attr, value, subValues);
    }
  }
}