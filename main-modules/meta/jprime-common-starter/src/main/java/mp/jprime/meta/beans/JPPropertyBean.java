package mp.jprime.meta.beans;

import mp.jprime.beans.PropertyType;
import mp.jprime.meta.JPProperty;

import java.util.Collection;
import java.util.Collections;

/**
 * Описание свойства псевдо-меты
 */
public class JPPropertyBean implements JPProperty {
  private final String code;
  private final PropertyType type;
  private final Integer length;
  private final boolean multiple;
  private final boolean mandatory;
  private final String name;
  private final String shortName;
  private final String description;
  private final String qName;
  private final String refJpClassCode;
  private final String refJpAttrCode;
  private final Collection<JPProperty> schemaProps;

  private JPPropertyBean(String code, PropertyType type, Integer length, boolean multiple,
                         boolean mandatory, String name, String shortName, String description, String qName,
                         String refJpClassCode, String refJpAttrCode, Collection<JPProperty> schemaProps) {
    this.code = code;
    this.type = type;
    this.length = length;
    this.multiple = multiple;
    this.mandatory = mandatory;
    this.name = name;
    this.shortName = shortName;
    this.description = description;
    this.qName = qName;
    this.refJpClassCode = refJpClassCode;
    this.refJpAttrCode = refJpAttrCode;
    this.schemaProps = schemaProps == null ? null : Collections.unmodifiableCollection(schemaProps);
  }

  /**
   * Кодовое имя свойства
   *
   * @return Кодовое имя свойства
   */
  @Override
  public String getCode() {
    return code;
  }

  /**
   * Возвращает признак обязательности
   *
   * @return Да/Нет
   */
  @Override
  public boolean isMandatory() {
    return mandatory;
  }

  /**
   * Возвращает признак множественности
   *
   * @return Да/Нет
   */
  @Override
  public boolean isMultiple() {
    return multiple;
  }

  /**
   * Тип свойства
   *
   * @return Тип свойства
   */
  @Override
  public PropertyType getType() {
    return type;
  }

  /**
   * Возвращает длину
   *
   * @return Длина
   */
  @Override
  public Integer getLength() {
    return length;
  }

  /**
   * Название свойства
   *
   * @return Название свойства
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Короткое название свойства
   *
   * @return Короткое название свойства
   */
  @Override
  public String getShortName() {
    return shortName;
  }

  /**
   * Описание свойства
   *
   * @return Описание свойства
   */
  @Override
  public String getDescription() {
    return description;
  }

  /**
   * Уникальный qName свойства
   *
   * @return Уникальный qName свойства
   */
  @Override
  public String getQName() {
    return qName;
  }

  /**
   * Код класса, на который ссылается
   *
   * @return Код класса, на который ссылается
   */
  @Override
  public String getRefJpClassCode() {
    return refJpClassCode;
  }

  /**
   * Код атрибута, на который ссылается
   *
   * @return Код атрибута, на который ссылается
   */
  @Override
  public String getRefJpAttrCode() {
    return refJpAttrCode;
  }

  /**
   * Список вложенных свойств
   *
   * @return список вложенных свойств
   */
  @Override
  public Collection<JPProperty> getSchemaProps() {
    return schemaProps;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private String code;
    private PropertyType type;
    private Integer length;
    private boolean multiple;
    private boolean mandatory;
    private String name;
    private String shortName;
    private String description;
    private String qName;
    private String refJpClassCode;
    private String refJpAttrCode;
    private Collection<JPProperty> schemaProps;

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
    public Builder type(PropertyType type) {
      this.type = type;
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
     * Признак множественности
     */
    public Builder multiple(boolean multiple) {
      this.multiple = multiple;
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
     * Короткое название свойства
     */
    public Builder shortName(String shortName) {
      this.shortName = shortName;
      return this;
    }

    /**
     * Описание свойства
     */
    public Builder description(String description) {
      this.description = description;
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
    public Builder refJpClassCode(String refJpClassCode) {
      this.refJpClassCode = refJpClassCode;
      return this;
    }

    /**
     * Код атрибута, на который ссылается
     */
    public Builder refJpAttrCode(String refJpAttrCode) {
      this.refJpAttrCode = refJpAttrCode;
      return this;
    }

    /**
     * Список вложенных свойств
     */
    public Builder schemaProps(Collection<JPProperty> schemaProps) {
      this.schemaProps = schemaProps;
      return this;
    }

    public JPPropertyBean build() {
      return new JPPropertyBean(code, type, length, multiple, mandatory, name, shortName,
          description, qName, refJpClassCode, refJpAttrCode, schemaProps);
    }
  }
}
