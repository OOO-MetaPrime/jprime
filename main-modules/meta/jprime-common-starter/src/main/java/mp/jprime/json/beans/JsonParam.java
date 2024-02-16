package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonParam {
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
   * Значение по умолчанию
   */
  private Object value;
  /**
   * Разрешен множественный выбор
   */
  private boolean multiple;
  /**
   *  Возможность внешнего переопределения параметра. Например, для ввода пользователем
   */
  private boolean external;
  /**
   * Класс из меты
   */
  private String refJpClass;
  /**
   * Атрибут класса
   */
  private String refJpAttr;
  /**
   * json для фильтрации объектов
   */
  private String refFilter;
  /**
   * Перечислимые значения
   */
  private Collection<JsonEnum> enums;
  /**
   * Разрешен множественный выбор
   */
  private boolean clientSearch;

  public JsonParam() {

  }

  private JsonParam(String code, String type, Integer length, String description, String qName,
                    boolean mandatory, Object value, boolean multiple, boolean external, String refJpClass,
                    String refJpAttr, String refFilter, Collection<JsonEnum> enums, boolean clientSearch) {
    this.code = code;
    this.type = type;
    this.length = length;
    this.description = description;
    this.qName = qName;
    this.mandatory = mandatory;
    this.value = value;
    this.multiple = multiple;
    this.external = external;
    this.refJpClass = refJpClass;
    this.refJpAttr = refJpAttr;
    this.refFilter = refFilter == null || refFilter.isEmpty() ? null : refFilter;
    this.enums = enums == null || enums.isEmpty() ? null : enums;
    this.clientSearch = clientSearch;
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

  /**
   * Должен ли параметр быть передан в POST-запросе
   */
  public boolean isMandatory() {
    return mandatory;
  }

  public Object getValue() {
    return value;
  }

  /**
   * Разрешен множественный выбор
   */
  public boolean isMultiple() {
    return multiple;
  }

  /**
   * Возможность внешнего переопределения параметра. Например, для ввода пользователем
   *
   * @return Да/Нет
   */
  public boolean isExternal() {
    return external;
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

  public Collection<JsonEnum> getEnums() {
    return enums;
  }

  public boolean isClientSearch() {
    return clientSearch;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private String code;
    private String type;
    private Integer length;
    private String description;
    private String qName;
    private boolean mandatory;
    private Object value;
    private boolean multiple;
    private boolean external = true;
    private String refJpClass;
    private String refJpAttr;
    private String refFilter;
    private Collection<JsonEnum> enums;
    private boolean clientSearch;

    private Builder() {
    }

    public JsonParam build() {
      return new JsonParam(code, type, length, description, qName, mandatory, value, multiple, external,
          refJpClass, refJpAttr, refFilter, enums, clientSearch);
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

    public Builder value(Object value) {
      this.value = value;
      return this;
    }

    public Builder  multiple(boolean multiple) {
      this.multiple = multiple;
      return this;
    }

    public Builder external(boolean external) {
      this.external = external;
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

    public Builder enums(Collection<JsonEnum> enums) {
      this.enums = enums;
      return this;
    }

    public Builder clientSearch(boolean clientSearch) {
      this.clientSearch = clientSearch;
      return this;
    }
  }
}
