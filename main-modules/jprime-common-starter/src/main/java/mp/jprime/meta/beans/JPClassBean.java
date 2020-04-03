package mp.jprime.meta.beans;

import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;

import java.util.*;

/**
 * метаописание класса
 */
public final class JPClassBean implements JPClass {
  private final String guid;
  private final String code;
  private final String pluralCode;
  /**
   * Название класса
   */
  private final String name;
  /**
   * Короткое название класса
   */
  private final String shortName;
  private final String description;
  private final String qName;
  private final String jpPackage;
  private final boolean inner;
  private final Map<String, JPAttr> attrs;
  private final JPAttr primaryKeyAttr;

  private JPClassBean(String guid, String code, String pluralCode,
                      String name, String shortName, String description, String qName,
                      String jpPackage, boolean inner, Collection<JPAttr> attrs) {
    this.guid = guid != null && !guid.isEmpty() ? guid : null;
    this.code = code != null && !code.isEmpty() ? code : null;
    this.pluralCode = pluralCode != null && !pluralCode.isEmpty() ? pluralCode : null;
    this.name = name != null && !name.isEmpty() ? name : null;
    this.shortName = shortName != null && !shortName.isEmpty() ? shortName : this.name;
    this.description = description != null && !description.isEmpty() ? description : this.name;
    this.qName = qName != null && !qName.isEmpty() ? qName : null;
    this.jpPackage = jpPackage != null && !jpPackage.isEmpty() ? jpPackage : null;
    this.inner = inner;

    // Ключевой атрибут
    JPAttr primaryKeyAttr = null;
    Map<String, JPAttr> attrMap = new HashMap<>();
    for (JPAttr attr : attrs) {
      attrMap.put(attr.getCode(), attr);
      if (attr.isIdentifier()) {
        primaryKeyAttr = attr;
      }
    }
    this.attrs = Collections.unmodifiableMap(attrMap);
    this.primaryKeyAttr = primaryKeyAttr;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    JPClassBean jpClass = (JPClassBean) o;
    return Objects.equals(code, jpClass.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code);
  }

  /**
   * Идентификатор/гуид класса
   *
   * @return Идентификатор/гуид класса
   */
  @Override
  public String getGuid() {
    return guid;
  }

  /**
   * Кодовое имя класса
   *
   * @return Кодовое имя класса
   */
  @Override
  public String getCode() {
    return code;
  }

  /**
   * Множественное кодовое имя класса
   *
   * @return Множественное кодовое имя класса
   */
  @Override
  public String getPluralCode() {
    return pluralCode;
  }

  /**
   * Название класса
   *
   * @return Название класса
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Короткое название класса
   *
   * @return Короткое название класса
   */
  @Override
  public String getShortName() {
    return shortName;
  }

  /**
   * Описание класса
   *
   * @return Описание класса
   */
  @Override
  public String getDescription() {
    return description;
  }

  /**
   * Уникальный qName класса
   *
   * @return Уникальный qName класса
   */
  @Override
  public String getQName() {
    return qName;
  }

  /**
   * Кодовое имя пакета/группировки метаописания класса
   *
   * @return Кодовое имя пакета/группировки метаописания класса
   */
  @Override
  public String getJpPackage() {
    return jpPackage;
  }

  /**
   * Признак класса только для внутренного доступа
   *
   * @return Признак класса только для внутренного доступа
   */
  @Override
  public boolean isInner() {
    return inner;
  }

  /**
   * Список атрибутов
   *
   * @return Список атрибутов
   */
  @Override
  public Collection<JPAttr> getAttrs() {
    return attrs.values();
  }

  /**
   * Возвращает атрибут по его кодовому имени
   *
   * @param code Кодовое имя атрибутоа
   * @return JPAttr
   */
  @Override
  public JPAttr getAttr(String code) {
    return attrs.get(code);
  }

  /**
   * Возвращает ключевой атрибут класса
   *
   * @return Ключевой атрибут
   */
  @Override
  public JPAttr getPrimaryKeyAttr() {
    return primaryKeyAttr;
  }

  @Override
  public String toString() {
    return "JPClass{" +
        "guid='" + guid + '\'' +
        ", code='" + code + '\'' +
        ", pluralCode='" + pluralCode + '\'' +
        ", name='" + name + '\'' +
        ", shortName='" + shortName + '\'' +
        ", name='" + description + '\'' +
        ", qName='" + qName + '\'' +
        ", attrs=" + attrs +
        '}';
  }

  /**
   * Построитель JPClass
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JPClass
   */
  public static final class Builder {
    private String guid;
    private String code;
    private String pluralCode;
    private String name;
    private String shortName;
    private String description;
    private String qName;
    private String jpPackage;
    private boolean inner;
    private Collection<JPAttr> attrs;

    private Builder() {
    }

    /**
     * Создаем метаописание
     *
     * @return Метаописание
     */
    public JPClassBean build() {
      return new JPClassBean(guid, code, pluralCode, name, shortName, description, qName, jpPackage, inner, attrs);
    }

    /**
     * Идентификатор/гуид класса
     *
     * @param guid Идентификатор/гуид класса
     * @return Builder
     */
    public Builder guid(String guid) {
      this.guid = guid;
      return this;
    }

    /**
     * Кодовое имя класса
     *
     * @param code Кодовое имя класса
     * @return Builder
     */
    public Builder code(String code) {
      this.code = code;
      return this;
    }

    /**
     * Множественное кодовое имя класса
     *
     * @param pluralCode Множественное кодовое имя класса
     * @return Builder
     */
    public Builder pluralCode(String pluralCode) {
      this.pluralCode = pluralCode;
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

    /**
     * Описание класса
     *
     * @param description Описание класса
     * @return Builder
     */
    public Builder description(String description) {
      this.description = description;
      return this;
    }

    /**
     * Уникальный qName класса
     *
     * @param qName Уникальный qName класса
     * @return Builder
     */
    public Builder qName(String qName) {
      this.qName = qName;
      return this;
    }

    /**
     * Кодовое имя пакета/группировки метаописания класса
     *
     * @param jpPackage Кодовое имя пакета/группировки метаописания класса
     * @return Builder
     */
    public Builder jpPackage(String jpPackage) {
      this.jpPackage = jpPackage;
      return this;
    }

    /**
     * Признак класса только для внутренного доступа
     *
     * @param inner Признак класса только для внутренного доступа
     * @return Builder
     */
    public Builder inner(boolean inner) {
      this.inner = inner;
      return this;
    }

    /**
     * Атрибуты класса
     *
     * @param attrs Атрибуты класса
     * @return Builder
     */
    public Builder attrs(Collection<JPAttr> attrs) {
      this.attrs = attrs;
      return this;
    }
  }
}
