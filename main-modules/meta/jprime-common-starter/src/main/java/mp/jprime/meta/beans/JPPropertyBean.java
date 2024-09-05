package mp.jprime.meta.beans;

import mp.jprime.beans.JPPropertyType;
import mp.jprime.common.JPEnum;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.meta.JPProperty;

import java.util.Collection;
import java.util.Collections;

/**
 * Описание свойства псевдо-меты
 */
public final class JPPropertyBean implements JPProperty {
  private final String code;
  private final JPPropertyType type;
  private final JPStringFormat stringFormat;
  private final String stringMask;
  private final Integer length;
  private final boolean mandatory;
  private final String name;
  private final String qName;
  private final String refJpClass;
  private final String refJpAttr;
  private final Filter filter;
  private final Collection<JPEnum> enums;
  private final Collection<JPProperty> jpProps;

  private JPPropertyBean(String code, JPPropertyType type,
                         JPStringFormat stringFormat, String stringMask, Integer length,
                         boolean mandatory, String name, String qName,
                         String refJpClass, String refJpAttr, Filter filter,
                         Collection<JPEnum> enums, Collection<JPProperty> jpProps) {
    this.code = code;
    this.type = type;
    this.stringFormat = stringFormat;
    this.stringMask = stringMask;
    this.length = length;
    this.mandatory = mandatory;
    this.name = name;
    this.qName = qName;
    this.refJpClass = refJpClass;
    this.refJpAttr = refJpAttr;
    this.filter = filter;
    this.enums = enums == null ? Collections.emptyList() : Collections.unmodifiableCollection(enums);
    this.jpProps = jpProps == null ? Collections.emptyList() : Collections.unmodifiableCollection(jpProps);
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public boolean isMandatory() {
    return mandatory;
  }

  @Override
  public JPPropertyType getType() {
    return type;
  }

  @Override
  public JPStringFormat getStringFormat() {
    return stringFormat;
  }

  @Override
  public String getStringMask() {
    return stringMask;
  }

  @Override
  public Integer getLength() {
    return length;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getQName() {
    return qName;
  }

  @Override
  public String getRefJpClass() {
    return refJpClass;
  }

  @Override
  public String getRefJpAttr() {
    return refJpAttr;
  }

  @Override
  public Filter getFilter() {
    return filter;
  }

  @Override
  public Collection<JPEnum> getEnums() {
    return enums;
  }

  @Override
  public Collection<JPProperty> getJpProps() {
    return jpProps;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private String code;
    private JPPropertyType type;
    private JPStringFormat stringFormat;
    private String stringMask;
    private Integer length;
    private boolean mandatory;
    private String name;
    private String qName;
    private String refJpClass;
    private String refJpAttr;
    private Filter filter;
    private Collection<JPEnum> enums;
    private Collection<JPProperty> jpProps;

    private Builder() {
    }

    /**
     * Кодовое имя свойства
     *
     * @return Кодовое имя свойства
     */
    public Builder code(String code) {
      this.code = code;
      return this;
    }

    /**
     * Тип свойства
     */
    public Builder type(JPPropertyType type) {
      this.type = type;
      return this;
    }

    /**
     * Тип строкового поля
     */
    public Builder stringFormat(JPStringFormat stringFormat) {
      this.stringFormat = stringFormat;
      return this;
    }

    /**
     * Маска строкового поля
     */
    public Builder stringMask(String stringMask) {
      this.stringMask = stringMask;
      return this;
    }

    /**
     * Длина (для строк)
     */
    public Builder length(Integer length) {
      this.length = length;
      return this;
    }


    /**
     * Признак обязательности
     */
    public Builder mandatory(boolean mandatory) {
      this.mandatory = mandatory;
      return this;
    }

    /**
     * Название свойства
     */
    public Builder name(String name) {
      this.name = name;
      return this;
    }

    /**
     * Уникальный qName свойства
     */
    public Builder qName(String qName) {
      this.qName = qName;
      return this;
    }

    /**
     * Код класса, на который ссылается
     */
    public Builder refJpClass(String refJpClass) {
      this.refJpClass = refJpClass;
      return this;
    }

    /**
     * Код атрибута, на который ссылается
     */
    public Builder refJpAttr(String refJpAttr) {
      this.refJpAttr = refJpAttr;
      return this;
    }

    /**
     * Условие, на объекты класса
     */
    public Builder filter(Filter filter) {
      this.filter = filter;
      return this;
    }

    /**
     * Перечислимые значения
     */
    public Builder enums(Collection<JPEnum> enums) {
      this.enums = enums;
      return this;
    }

    /**
     * Список вложенных свойств
     */
    public Builder jpProps(Collection<JPProperty> jpProps) {
      this.jpProps = jpProps;
      return this;
    }

    public JPPropertyBean build() {
      return new JPPropertyBean(code, type, stringFormat, stringMask, length,
          mandatory, name, qName, refJpClass, refJpAttr, filter, enums, jpProps);
    }
  }
}
