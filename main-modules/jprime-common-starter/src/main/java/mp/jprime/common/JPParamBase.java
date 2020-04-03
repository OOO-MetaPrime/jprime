package mp.jprime.common;

import mp.jprime.meta.beans.JPType;

import java.util.Collection;
import java.util.Collections;

/**
 * Бин параметра для отчета
 */
public class JPParamBase implements JPParam {
  /**
   * Код
   */
  private final String code;
  /**
   * Тип
   */
  private final JPType type;
  /**
   * Длина (для строковых полей)
   */
  private final Integer length;
  /**
   * Название
   */
  private final String description;
  /**
   * QName
   */
  private final String qName;
  /**
   * Признак обязательности
   */
  private final boolean mandatory;
  /**
   * Разрешен ли множественный выбор
   */
  private final boolean multiple;
  /**
   * Класс из меты
   */
  private final String refJpClass;
  /**
   * Атрибут класса
   */
  private final String refJpAttr;
  /**
   * json для фильтрации объектов
   */
  private final String refFilter;
  /**
   * Возможность внешнего переопределения параметра. Например, для ввода пользователем
   */
  private final boolean external;
  /**
   * Значение параметра
   */
  private final Object value;
  /**
   * Перечислимые значения
   */
  private final Collection<JPEnum> enums;

  /**
   * @param code        Название(Код) параметра
   * @param type        Тип параметра
   * @param length      Длина (для строковых полей)
   * @param description Наименование
   * @param qName       QName
   * @param mandatory   Признак обязательности
   * @param refJpClass  Класс из меты
   * @param refJpAttr   Атрибут класса
   * @param refFilter   JSON фильтрации объектов
   * @param external    Возможность внешнего переопределения параметра
   * @param value       Значение параметра
   * @param enums       Перечислимые значения
   */
  protected JPParamBase(String code, JPType type, Integer length, String description, String qName,
                        boolean mandatory, boolean multiple, String refJpClass, String refJpAttr, String refFilter,
                        boolean external, Object value, Collection<JPEnum> enums) {
    this.code = code;
    this.type = type;
    this.length = length != null && length > 0 ? length : null;
    this.description = description;
    this.qName = qName;
    this.mandatory = mandatory;
    this.multiple = multiple;
    this.refJpClass = refJpClass != null && !refJpClass.isEmpty() ? refJpClass : null;
    this.refJpAttr = refJpAttr != null && !refJpAttr.isEmpty() ? refJpAttr : null;
    this.refFilter = refFilter != null && !refFilter.isEmpty() ? refFilter : null;
    this.external = external;
    this.value = value;
    this.enums = Collections.unmodifiableCollection(enums != null ? enums : Collections.emptyList());
    ;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public JPType getType() {
    return type;
  }

  @Override
  public Integer getLength() {
    return length;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public String getQName() {
    return qName;
  }

  @Override
  public String getRefJpClassCode() {
    return refJpClass;
  }

  @Override
  public String getRefJpAttrCode() {
    return refJpAttr;
  }

  @Override
  public String getRefFilter() {
    return refFilter;
  }

  @Override
  public boolean isMandatory() {
    return mandatory;
  }

  @Override
  public boolean isMultiple() {
    return multiple;
  }

  @Override
  public boolean isExternal() {
    return external;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public Collection<JPEnum> getEnums() {
    return enums;
  }

  public static Builder newBuilder() {
    return new Builder<>();
  }

  protected static class Builder<T extends Builder> {
    protected String code;
    protected JPType type;
    protected Integer length;
    protected String description;
    protected String qName;
    protected boolean mandatory;
    protected boolean multiple;
    protected String refJpClass;
    protected String refJpAttr;
    protected String refFilter;
    protected boolean external;
    protected Object value;
    protected Collection<JPEnum> enums;

    protected Builder() {
    }

    public JPParamBase build() {
      return new JPParamBase(code, type, length, description, qName, mandatory, multiple,
          refJpClass, refJpAttr, refFilter, external, value, enums);
    }

    public T enums(Collection<JPEnum> enums) {
      this.enums = enums;
      return (T) this;
    }

    public T code(String code) {
      this.code = code;
      return (T) this;
    }

    public T type(JPType type) {
      this.type = type;
      return (T) this;
    }

    public T length(Integer length) {
      this.length = length;
      return (T) this;
    }

    public T description(String description) {
      this.description = description;
      return (T) this;
    }

    public T qName(String qName) {
      this.qName = qName;
      return (T) this;
    }

    public T mandatory(boolean mandatory) {
      this.mandatory = mandatory;
      return (T) this;
    }

    public T multiple(boolean multiple) {
      this.multiple = multiple;
      return (T) this;
    }

    public T refJpClass(String refJpClass) {
      this.refJpClass = refJpClass;
      return (T) this;
    }

    public T refJpAttr(String refJpAttr) {
      this.refJpAttr = refJpAttr;
      return (T) this;
    }

    public T refFilter(String refFilter) {
      this.refFilter = refFilter;
      return (T) this;
    }

    public T external(boolean external) {
      this.external = external;
      return (T) this;
    }

    public T value(Object value) {
      this.value = value;
      return (T) this;
    }
  }
}
