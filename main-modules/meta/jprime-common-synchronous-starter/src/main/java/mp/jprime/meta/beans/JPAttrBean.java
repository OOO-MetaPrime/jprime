package mp.jprime.meta.beans;

import mp.jprime.formats.JPStringFormat;
import mp.jprime.meta.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * Метаописание атрибута
 */
public final class JPAttrBean implements JPAttr {
  private final String jpClassCode;
  private final String guid;
  private final String code;
  private final JPType type;
  private final JPStringFormat stringFormat;
  private final String stringMask;
  private final Integer length;
  private final boolean identifier;
  private final boolean mandatory;
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
  private final String refJpClass;
  private final String refJpAttr;
  private final JPFile refJpFile;
  private final JPSimpleFraction simpleFraction;
  private final JPMoney money;
  private final JPGeometry geometry;
  private final JPVirtualPath virtualReference;
  private final String signAttrCode;

  private JPAttrBean(String jpClassCode, String guid, String code, JPType type,
                     JPStringFormat stringFormat, String stringMask, Integer length,
                     boolean identifier, boolean mandatory,
                     String name, String shortName, String description,
                     String qName, String jpPackage,
                     String refJpClass, String refJpAttr,
                     JPFile refJpFile, JPSimpleFraction simpleFraction, JPMoney money,
                     JPGeometry geometry, JPVirtualPath virtualReference, String signAttrCode) {
    this.jpClassCode = jpClassCode != null && !jpClassCode.isEmpty() ? jpClassCode : null;
    this.guid = guid != null && !guid.isEmpty() ? guid : null;
    this.code = code != null && !code.isEmpty() ? code : null;
    this.type = type;
    this.name = name != null && !name.isEmpty() ? name : null;
    this.shortName = shortName != null && !shortName.isEmpty() ? shortName : this.name;
    this.description = description != null && !description.isEmpty() ? description : this.name;
    this.qName = qName != null && !qName.isEmpty() ? qName : null;
    this.jpPackage = jpPackage != null && !jpPackage.isEmpty() ? jpPackage : null;
    this.refJpClass = refJpClass != null && !refJpClass.isEmpty() ? refJpClass : null;
    this.refJpAttr = refJpAttr != null && !refJpAttr.isEmpty() ? refJpAttr : null;
    this.refJpFile = refJpFile;
    this.simpleFraction = simpleFraction;
    this.money = money;
    this.geometry = geometry;
    this.virtualReference = virtualReference;

    this.stringFormat = stringFormat != JPStringFormat.NONE ? stringFormat : null;
    this.stringMask = stringMask != null && !stringMask.isBlank() ? stringMask : null;
    this.length = length;
    this.identifier = identifier;
    this.mandatory = mandatory;
    this.signAttrCode = StringUtils.isBlank(signAttrCode) ? null : signAttrCode;
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

  @Override
  public String getJpClassCode() {
    return jpClassCode;
  }

  @Override
  public String getGuid() {
    return guid;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public boolean isIdentifier() {
    return identifier;
  }

  @Override
  public boolean isMandatory() {
    return mandatory;
  }

  @Override
  public JPType getType() {
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
  public String getShortName() {
    return shortName;
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
  public String getJpPackage() {
    return jpPackage;
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
  public JPFile getRefJpFile() {
    return refJpFile;
  }

  @Override
  public JPSimpleFraction getSimpleFraction() {
    return simpleFraction;
  }

  @Override
  public JPMoney getMoney() {
    return money;
  }

  @Override
  public JPGeometry getGeometry() {
    return geometry;
  }

  @Override
  public JPVirtualPath getVirtualReference() {
    return virtualReference;
  }

  @Override
  public String getSignAttrCode() {
    return signAttrCode;
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
        ", refJpClass='" + refJpClass + '\'' +
        ", refJpAttr='" + refJpAttr + '\'' +
        (refJpFile != null ? ", refJpFile='" + refJpFile + '\'' : "") +
        (simpleFraction != null ? ", simpleFraction='" + simpleFraction + '\'' : "") +
        (money != null ? ", money='" + money + '\'' : "") +
        (geometry != null ? ", geometry='" + geometry + '\'' : "") +
        ", virtualReference='" + virtualReference + '\'' +
        (length != null ? ", length='" + length + '\'' : "") +
        ", signAttrCode='" + signAttrCode +
        '}';
  }

  /**
   * Построитель JPAttr
   */
  public static final class Builder {
    private String jpClassCode;
    private String guid;
    private String code;
    private JPType type;
    private JPStringFormat stringFormat;
    private String stringMask;
    private Integer length;
    private boolean identifier;
    private boolean mandatory;
    private String name;
    private String shortName;
    private String description;
    private String qName;
    private String jpPackage;
    private String refJpClass;
    private String refJpAttr;
    private JPFile refJpFile;
    private JPSimpleFraction simpleFraction;
    private JPMoney money;
    private JPGeometry geometry;
    private JPVirtualPath virtualReference;
    private String signAttrCode;

    private Builder() {
    }

    /**
     * Создаем метаописание
     *
     * @return Метаописание
     */
    public JPAttrBean build() {
      return new JPAttrBean(jpClassCode, guid, code, type,
          stringFormat, stringMask, length, identifier, mandatory,
          name, shortName, description, qName, jpPackage,
          refJpClass, refJpAttr, refJpFile, simpleFraction, money, geometry, virtualReference, signAttrCode);
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
     * @param refJpClass Код класса, на который ссылается
     * @return Builder
     */
    public Builder refJpClass(String refJpClass) {
      this.refJpClass = refJpClass;
      return this;
    }

    /**
     * Код атрибута, на который ссылается
     *
     * @param refJpAttr Код атрибута, на который ссылается
     * @return Builder
     */
    public Builder refJpAttr(String refJpAttr) {
      this.refJpAttr = refJpAttr;
      return this;
    }

    /**
     * Путь виртуальной ссылки
     *
     * @param virtualReference Путь виртуальной ссылки
     * @return Builder
     */
    public Builder virtualReference(JPVirtualPath virtualReference) {
      this.virtualReference = virtualReference;
      return this;
    }

    /**
     * Настройки хранения файла
     *
     * @param refJpFile Настройки хранения файла
     * @return Builder
     */
    public Builder refJpFile(JPFile refJpFile) {
      this.refJpFile = refJpFile;
      return this;
    }

    /**
     * Настройки простой дроби
     *
     * @param simpleFraction Настройки простой дроби
     * @return Builder
     */
    public Builder simpleFraction(JPSimpleFraction simpleFraction) {
      this.simpleFraction = simpleFraction;
      return this;
    }

    /**
     * Настройки денежного типа
     *
     * @param money Настройки денежного типа
     * @return Builder
     */
    public Builder money(JPMoney money) {
      this.money = money;
      return this;
    }

    /**
     * Настройки пространственных данных
     *
     * @param geometry Настройки пространственных данных
     * @return Builder
     */
    public Builder geometry(JPGeometry geometry) {
      this.geometry = geometry;
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
    public Builder type(JPType type) {
      this.type = type;
      return this;
    }

    /**
     * Тип атрибута
     *
     * @param type Тип атрибута
     * @return Builder
     */
    public Builder type(String type) {
      this.type = type != null && !type.isEmpty() ? JPType.getType(type) : null;
      return this;
    }

    /**
     * Тип строкового поля
     *
     * @param stringFormat Тип строкового поля
     * @return Builder
     */
    public Builder stringFormat(JPStringFormat stringFormat) {
      this.stringFormat = stringFormat;
      return this;
    }

    /**
     * Маска строкового поля
     *
     * @param stringMask Маска строкового поля
     * @return Builder
     */
    public Builder stringMask(String stringMask) {
      this.stringMask = stringMask;
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

    /**
     * Код атрибута, содержащего подпись
     *
     * @param signAttrCode Код атрибута, содержащего подпись
     * @return Builder
     */
    public Builder signAttrCode(String signAttrCode) {
      this.signAttrCode = signAttrCode;
      return this;
    }
  }
}
