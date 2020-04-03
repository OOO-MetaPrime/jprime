package mp.jprime.meta.beans;

import mp.jprime.meta.JPAttr;

import java.util.Objects;

/**
 * метаописание атрибута
 */
public final class JPAttrBean implements JPAttr {
  private final String jpClassCode;
  private final String guid;
  private final String code;
  private final JPType type;
  private final Integer length;
  private final boolean identifier;
  private final boolean mandatory;
  private final String virtualReference;
  private final JPType virtualType;
  /**
   * Название атрибута
   */
  private final String name;
  /**
   * Короткое название атрибута
   */
  private final String shortName;
  private final String description;
  private final String qName;
  private final String jpPackage;
  private final String refJpClassCode;
  private final String refJpAttrCode;

  private JPAttrBean(String jpClassCode, String guid, String code, String type, Integer length, boolean identifier,
                     boolean mandatory,
                     String name, String shortName, String description,
                     String qName, String jpPackage, String refJpClassCode, String refJpAttrCode,
                     String virtualReference, String virtualType) {
    this.jpClassCode = jpClassCode != null && !jpClassCode.isEmpty() ? jpClassCode : null;
    this.guid = guid != null && !guid.isEmpty() ? guid : null;
    this.code = code != null && !code.isEmpty() ? code : null;
    this.type = type != null && !type.isEmpty() ? JPType.getType(type) : null;
    this.name = name != null && !name.isEmpty() ? name : null;
    this.shortName = shortName != null && !shortName.isEmpty() ? shortName : this.name;
    this.description = description != null && !description.isEmpty() ? description : this.name;
    this.qName = qName != null && !qName.isEmpty() ? qName : null;
    this.jpPackage = jpPackage != null && !jpPackage.isEmpty() ? jpPackage : null;
    this.refJpClassCode = refJpClassCode != null && !refJpClassCode.isEmpty() ? refJpClassCode : null;
    this.refJpAttrCode = refJpAttrCode != null && !refJpAttrCode.isEmpty() ? refJpAttrCode : null;
    this.virtualReference = virtualReference != null && !virtualReference.isEmpty() ? virtualReference : null;
    this.virtualType = virtualType != null && !virtualType.isEmpty() ? JPType.getType(virtualType) : null;

    this.length = length;
    this.identifier = identifier;
    this.mandatory = mandatory;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    JPAttrBean jpAttr = (JPAttrBean) o;
    return Objects.equals(jpClassCode, jpAttr.jpClassCode) &&
        Objects.equals(code, jpAttr.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jpClassCode, code);
  }

  /**
   * Код класса
   *
   * @return Код класса
   */
  @Override
  public String getJpClassCode() {
    return jpClassCode;
  }

  /**
   * Идентификатор/гуид атрибута
   *
   * @return Идентификатор/гуид атрибута
   */
  @Override
  public String getGuid() {
    return guid;
  }

  /**
   * Кодовое имя атрибута
   *
   * @return Кодовое имя атрибута
   */
  @Override
  public String getCode() {
    return code;
  }

  /**
   * Возвращает признак идентификатора
   *
   * @return Да/Нет
   */
  @Override
  public boolean isIdentifier() {
    return identifier;
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
   * Тип атрибута
   *
   * @return Тип атрибута
   */
  @Override
  public JPType getType() {
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
   * Название атрибута
   *
   * @return Название атрибута
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Короткое название атрибута
   *
   * @return Короткое название атрибута
   */
  @Override
  public String getShortName() {
    return shortName;
  }

  /**
   * Описание атрибута
   *
   * @return Описание атрибута
   */
  @Override
  public String getDescription() {
    return description;
  }

  /**
   * Уникальный qName атрибута
   *
   * @return Уникальный qName атрибута
   */
  @Override
  public String getQName() {
    return qName;
  }

  /**
   * Кодовое имя пакета/группировки метаописания атрибута
   *
   * @return Кодовое имя пакета/группировки метаописания атрибута
   */
  @Override
  public String getJpPackage() {
    return jpPackage;
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
   * Путь виртуальной ссылки
   *
   * @return Путь виртуальной ссылки
   */
  @Override
  public String getVirtualReference() {
    return virtualReference;
  }

  /**
   * Тип виртуальной ссылки
   *
   * @return Тип виртуальной ссылки
   */
  @Override
  public JPType getVirtualType() {
    return virtualType;
  }

  /**
   * Построитель JPAttr
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  @Override
  public String toString() {
    return "JPAttr{" +
        "jpClassCode='" + jpClassCode + '\'' +
        ", guid='" + guid + '\'' +
        ", code='" + code + '\'' +
        ", type='" + type + '\'' +
        ", name='" + name + '\'' +
        ", shortName='" + shortName + '\'' +
        ", name='" + description + '\'' +
        ", qName='" + qName + '\'' +
        ", refJpClassCode='" + refJpClassCode + '\'' +
        ", refJpAttrCode='" + refJpAttrCode + '\'' +
        ", virtualReference='" + virtualReference + '\'' +
        ", virtualType='" + virtualType + '\'' +
        (length != null ? ", length='" + length + '\'' : "") +
        '}';
  }

  /**
   * Построитель JPAttr
   */
  public static final class Builder {
    private String jpClassCode;
    private String guid;
    private String code;
    private String type;
    private Integer length;
    private boolean identifier;
    private boolean mandatory;
    private String name;
    private String shortName;
    private String description;
    private String qName;
    private String jpPackage;
    private String refJpClassCode;
    private String refJpAttrCode;
    private String virtualReference;
    private String virtualType;

    private Builder() {
    }

    /**
     * Создаем метаописание
     *
     * @return Метаописание
     */
    public JPAttrBean build() {
      return new JPAttrBean(jpClassCode, guid, code, type, length, identifier, mandatory,
          name, shortName, description, qName, jpPackage,
          refJpClassCode, refJpAttrCode, virtualReference, virtualType);
    }

    /**
     * Код класса
     *
     * @param jpClassCode Код класса
     * @return Builder
     */
    public Builder jpClassCode(String jpClassCode) {
      this.jpClassCode = jpClassCode;
      return this;
    }

    /**
     * Код класса, на который ссылается
     *
     * @param refJpClassCode Код класса, на который ссылается
     * @return Builder
     */
    public Builder refJpClassCode(String refJpClassCode) {
      this.refJpClassCode = refJpClassCode;
      return this;
    }

    /**
     * Код атрибута, на который ссылается
     *
     * @param refJpAttrCode Код атрибута, на который ссылается
     * @return Builder
     */
    public Builder refJpAttrCode(String refJpAttrCode) {
      this.refJpAttrCode = refJpAttrCode;
      return this;
    }

    /**
     * Путь виртуальной ссылки
     *
     * @param virtualReference Путь виртуальной ссылки
     * @return Builder
     */
    public Builder virtualReference(String virtualReference) {
      this.virtualReference = virtualReference;
      return this;
    }

    /**
     * Тип виртуальной ссылки
     *
     * @param virtualType Тип виртуальной ссылки
     * @return Builder
     */
    public Builder virtualType(String virtualType) {
      this.virtualType = virtualType;
      return this;
    }

    /**
     * Идентификатор/гуид атрибута
     *
     * @param guid Идентификатор/гуид атрибута
     * @return Builder
     */
    public Builder guid(String guid) {
      this.guid = guid;
      return this;
    }

    /**
     * Кодовое имя атрибута
     *
     * @param code Кодовое имя атрибута
     * @return Builder
     */
    public Builder code(String code) {
      this.code = code;
      return this;
    }

    /**
     * Тип атрибута
     *
     * @param type Тип атрибута
     * @return Builder
     */
    public Builder type(String type) {
      this.type = type;
      return this;
    }

    /**
     * Длина атрибута
     *
     * @param length Длина атрибута
     * @return Builder
     */
    public Builder length(Integer length) {
      this.length = length != null && length > 0 ? length : null;
      return this;
    }

    /**
     * Признак идентификатора
     *
     * @param identifier Да/Нет
     * @return Builder
     */
    public Builder identifier(boolean identifier) {
      this.identifier = identifier;
      return this;
    }

    /**
     * Признак обязательности
     *
     * @param mandatory Да/Нет
     * @return Builder
     */
    public Builder mandatory(boolean mandatory) {
      this.mandatory = mandatory;
      return this;
    }

    /**
     * Название атрибута
     *
     * @param name Название атрибута
     * @return Название атрибута
     */
    public Builder name(String name) {
      this.name = name;
      return this;
    }

    /**
     * Короткое название атрибута
     *
     * @param shortName Короткое название атрибута
     * @return Короткое название атрибута
     */
    public Builder shortName(String shortName) {
      this.shortName = shortName;
      return this;
    }

    /**
     * Описание атрибута
     *
     * @param description Описание атрибута
     * @return Builder
     */
    public Builder description(String description) {
      this.description = description;
      return this;
    }

    /**
     * Уникальный qName атрибута
     *
     * @param qName Уникальный qName атрибута
     * @return Builder
     */
    public Builder qName(String qName) {
      this.qName = qName;
      return this;
    }

    /**
     * Кодовое имя пакета/группировки метаописания атрибута
     *
     * @param jpPackage Кодовое имя пакета/группировки метаописания атрибута
     * @return Builder
     */
    public Builder jpPackage(String jpPackage) {
      this.jpPackage = jpPackage;
      return this;
    }
  }
}
