package mp.jprime.meta.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonJPAttr {
  /**
   * Глобальный идентификатор
   */
  private String guid;
  /**
   * Кодовое имя атрибута
   */
  private String code;
  /**
   * Полный код атрибута
   */
  private String qName;
  /**
   * Название атрибута
   */
  private String name;
  /**
   * Короткое название атрибута
   */
  private String shortName;
  /**
   * Описание атрибута
   */
  private String description;
  /**
   * Настройки доступа
   */
  private String jpPackage;
  /**
   * Признак идентификатора
   */
  private boolean identifier;
  /**
   * Признак обязательности
   */
  private boolean mandatory;
  /**
   * Тип атрибута
   */
  private String type;
  /**
   * Длина (для строковых полей)
   */
  private Integer length;
  /**
   * Кодовое имя класса, на который ссылается
   */
  private String refJpClass;
  /**
   * Кодовое имя атрибута ссылочного класса
   */
  private String refJpAttr;

  public JsonJPAttr() {

  }

  private JsonJPAttr(String guid, String code, String qName,
                     String name, String shortName, String description,
                     String jpPackage, boolean identifier, boolean mandatory,
                     String type, Integer length, String refJpClass, String refJpAttr) {
    this.guid = guid;
    this.code = code;
    this.qName = qName;
    this.name = name;
    this.shortName = shortName;
    this.description = description;
    this.jpPackage = jpPackage;
    this.identifier = identifier;
    this.mandatory = mandatory;
    this.type = type;
    this.length = length;
    this.refJpClass = refJpClass;
    this.refJpAttr = refJpAttr;
  }

  /**
   * Глобальный идентификатор
   *
   * @return Глобальный идентификатор
   */
  public String getGuid() {
    return guid;
  }

  /**
   * Кодовое имя атрибута
   *
   * @return Кодовое имя атрибута
   */
  public String getCode() {
    return code;
  }

  /**
   * Полный код атрибута
   *
   * @return Полный код атрибута
   */
  public String getqName() {
    return qName;
  }

  /**
   * Название атрибута
   *
   * @return Название атрибута
   */
  public String getName() {
    return name;
  }

  /**
   * Короткое название атрибута
   *
   * @return Короткое название атрибута
   */
  public String getShortName() {
    return shortName;
  }

  /**
   * Описание атрибута
   *
   * @return Описание атрибута
   */
  public String getDescription() {
    return description;
  }

  /**
   * Настройки доступа
   *
   * @return Настройки доступа
   */
  public String getJpPackage() {
    return jpPackage;
  }

  /**
   * Признак идентификатора
   *
   * @return Признак идентификатора
   */
  public boolean isIdentifier() {
    return identifier;
  }

  /**
   * Признак обязательности
   *
   * @return Признак обязательности
   */
  public boolean isMandatory() {
    return mandatory;
  }

  /**
   * Тип атрибута
   *
   * @return Тип атрибута
   */
  public String getType() {
    return type;
  }

  /**
   * Длина (для строковых полей)
   *
   * @return Длина (для строковых полей)
   */
  public Integer getLength() {
    return length;
  }

  /**
   * Кодовое имя класса, на который ссылается
   *
   * @return Кодовое имя класса, на который ссылается
   */
  public String getRefJpClass() {
    return refJpClass;
  }

  /**
   * Кодовое имя атрибута ссылочного класса
   *
   * @return Кодовое имя атрибута ссылочного класса
   */
  public String getRefJpAttr() {
    return refJpAttr;
  }

  /**
   * Построитель JsonJPAttr
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JsonJPAttr
   */
  public static final class Builder {
    private String guid;
    private String code;
    private String qName;
    private String name;
    private String shortName;
    private String description;
    private String jpPackage;
    private boolean identifier;
    private boolean mandatory;
    private String type;
    private String refJpClass;
    private String refJpAttr;
    private Integer length;

    private Builder() {

    }

    public Builder guid(String guid) {
      this.guid = guid;
      return this;
    }

    public Builder code(String code) {
      this.code = code;
      return this;
    }

    public Builder qName(String qName) {
      this.qName = qName;
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

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder jpPackage(String jpPackage) {
      this.jpPackage = jpPackage;
      return this;
    }

    public Builder identifier(boolean identifier) {
      this.identifier = identifier;
      return this;
    }

    public Builder mandatory(boolean mandatory) {
      this.mandatory = mandatory;
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

    public Builder refJpClass(String refJpClass) {
      this.refJpClass = refJpClass;
      return this;
    }

    public Builder refJpAttr(String refJpAttr) {
      this.refJpAttr = refJpAttr;
      return this;
    }

    public JsonJPAttr build() {
      return new JsonJPAttr(guid, code, qName, name, shortName, description, jpPackage, identifier, mandatory,
          type, length, refJpClass, refJpAttr);
    }
  }
}