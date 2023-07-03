package mp.jprime.utils.json;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class JsonUtilParam {
  /**
   * Код
   */
  private String code;
  /**
   * Тип
   */
  private String type;
  /**
   * Длина для строковых полей
   */
  private Integer length;
  /**
   * Название
   */
  private String description;
  /**
   * QName
   */
  private String qName;
  /**
   * Признак обязательности
   */
  private boolean mandatory;
  /**
   * Разрешен ли множественный выбор
   */
  private boolean multiple;
  /**
   * Класс из меты
   */
  private String refJpClass;
  /**
   * Атрибут класса
   */
  private String refJpAttr;
  /**
   * JSON для выборки объекта
   */
  private String refFilter;
  /**
   * Перечислимые значения
   */
  private Collection<JsonUtilEnum> enums;

  public JsonUtilParam() {

  }

  private JsonUtilParam(String code, String type, Integer length, String description, String qName,
                        boolean mandatory, boolean multiple, String refJpClass, String refJpAttr, String refFilter,
                        Collection<JsonUtilEnum> enums) {
    this.code = code;
    this.type = type;
    this.length = length;
    this.description = description;
    this.qName = qName;
    this.mandatory = mandatory;
    this.multiple = multiple;
    this.refJpClass = refJpClass;
    this.refJpAttr = refJpAttr;
    this.refFilter = refFilter == null || refFilter.isEmpty() ? null : refFilter;
    this.enums = enums == null || enums.isEmpty() ? null : enums;
  }

  public String getCode() {
    return code;
  }

  public String getType() {
    return type;
  }

  public Integer getLength() {
    return length;
  }

  public String getDescription() {
    return description;
  }

  public String getqName() {
    return qName;
  }

  public boolean isMandatory() {
    return mandatory;
  }

  public boolean isMultiple() {
    return multiple;
  }

  public String getRefJpClass() {
    return refJpClass;
  }

  public String getRefJpAttr() {
    return refJpAttr;
  }

  public String getRefFilter() {
    return refFilter;
  }

  public Collection<JsonUtilEnum> getEnums() {
    return enums;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private String code;
    private String type;
    private Integer length;
    private String description;
    private String qName;
    private boolean mandatory;
    private boolean multiple;
    private String refJpClass;
    private String refJpAttr;
    private String refFilter;
    private Collection<JsonUtilEnum> enums;

    private Builder() {
    }

    public JsonUtilParam build() {
      return new JsonUtilParam(code, type, length, description, qName, mandatory, multiple, refJpClass, refJpAttr, refFilter, enums);
    }

    public Builder code(String code) {
      this.code = code;
      return this;
    }

    public Builder type(String type) {
      this.type = type;
      return this;
    }

    public Builder length(Integer length) {
      this.length = length;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder qName(String qName) {
      this.qName = qName;
      return this;
    }

    public Builder mandatory(boolean mandatory) {
      this.mandatory = mandatory;
      return this;
    }

    public Builder multiple(boolean multiple) {
      this.multiple = multiple;
      return this;
    }

    public Builder refJpClass(String refJpClass) {
      this.refJpClass = refJpClass;
      return this;
    }

    public Builder refJpAttr(String refJpAttr) {
      this.refJpAttr = refJpAttr;
      return this;
    }

    public Builder refFilter(String refFilter) {
      this.refFilter = refFilter;
      return this;
    }

    public Builder enums(Collection<JsonUtilEnum> enums) {
      this.enums = enums;
      return this;
    }
  }
}
