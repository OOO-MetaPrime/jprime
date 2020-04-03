package mp.jprime.meta.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class JsonJPClass {
  /**
   * Глобальный идентификатор
   */
  private String guid;
  /**
   * Кодовое имя
   */
  private String code;
  /**
   * Множественный код кодового имени
   */
  private String pluralCode;
  /**
   * Полный код класса
   */
  private String qName;
  /**
   * Название класса
   */
  private String name;
  /**
   * Короткое название класса
   */
  private String shortName;
  /**
   * Описание класса
   */
  private String description;
  /**
   * Настройки доступа
   */
  private String jpPackage;
  /**
   * Признак неизменяемости метаописания
   */
  private boolean immutable;
  /**
   * Список атрибутов класса
   */
  private Collection<JsonJPAttr> attrs;
  /**
   * Маппинг класса
   */
  private Collection<JsonJPClassMap> maps;

  public JsonJPClass() {

  }

  private JsonJPClass(String code, String guid, String pluralCode, String qName,
                      String name, String shortName, String description, String jpPackage,
                      boolean immutable, Collection<JsonJPAttr> attrs, Collection<JsonJPClassMap> maps) {
    this.code = code;
    this.guid = guid;
    this.pluralCode = pluralCode;
    this.qName = qName;
    this.name = name;
    this.shortName = shortName;
    this.description = description;
    this.jpPackage = jpPackage;
    this.immutable = immutable;
    this.attrs = attrs;
    this.maps = maps;
  }

  /**
   * Кодовое имя
   *
   * @return Кодовое имя
   */
  public String getCode() {
    return code;
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
   * Множественный код кодового имени
   *
   * @return Множественный код кодового имени
   */
  public String getPluralCode() {
    return pluralCode;
  }

  /**
   * Полный код класса
   *
   * @return Полный код класса
   */
  public String getqName() {
    return qName;
  }

  /**
   * Название класса
   *
   * @return Название класса
   */
  public String getName() {
    return name;
  }

  /**
   * Короткое название класса
   *
   * @return Короткое название класса
   */
  public String getShortName() {
    return shortName;
  }

  /**
   * Описание класса
   *
   * @return Описание класса
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
   * Список атрибутов класса
   *
   * @return Список атрибутов класса
   */
  @JsonInclude(JsonInclude.Include.NON_DEFAULT)
  public Collection<JsonJPAttr> getAttrs() {
    return attrs;
  }

  /**
   * Маппинг класса
   *
   * @return Маппинг класса
   */
  @JsonInclude(JsonInclude.Include.NON_DEFAULT)
  public Collection<JsonJPClassMap> getMaps() {
    return maps;
  }

  /**
   * Признак неизменности класса
   *
   * @return Да/Нет
   */
  public boolean isImmutable() {
    return immutable;
  }

  /**
   * Построитель JsonJPClass
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JsonJPClass
   */
  public static final class Builder {
    private String guid;
    private String code;
    private String pluralCode;
    private String qName;
    private String name;
    private String shortName;
    private String description;
    private String jpPackage;
    private boolean immutable;
    private Collection<JsonJPAttr> attrs;
    private Collection<JsonJPClassMap> maps;

    private Builder() {

    }

    public Builder code(String code) {
      this.code = code;
      return this;
    }

    public Builder guid(String guid) {
      this.guid = guid;
      return this;
    }

    public Builder pluralCode(String pluralCode) {
      this.pluralCode = pluralCode;
      return this;
    }

    public Builder qName(String qName) {
      this.qName = qName;
      return this;
    }

    /**
     * Название класса
     *
     * @param name Название класса
     * @return Название класса
     */
    public Builder name(String name) {
      this.name = name;
      return this;
    }

    /**
     * Короткое название класса
     *
     * @param shortName Короткое название класса
     * @return Короткое название класса
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

    public Builder immutable(boolean immutable) {
      this.immutable = immutable;
      return this;
    }

    public Builder attrs(Collection<JsonJPAttr> attrs) {
      this.attrs = attrs;
      return this;
    }

    public Builder maps(Collection<JsonJPClassMap> maps) {
      this.maps = maps;
      return this;
    }

    public JsonJPClass build() {
      return new JsonJPClass(code, guid, pluralCode, qName, name, shortName, description, jpPackage,
          immutable, attrs, maps);
    }
  }
}
