package mp.jprime.security.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;

/**
 * Описание всех пакетов доступа
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class JsonSecurityPackage {
  private String guid;
  private String code;
  private String name;
  private String description;
  private String qName;
  private Collection<JsonSecurityAccess> accesses;

  public JsonSecurityPackage() {

  }

  private JsonSecurityPackage(
      String guid, String code, String name, String description, String qName,
      Collection<JsonSecurityAccess> accesses
  ) {
    this.guid = guid;
    this.code = code;
    this.name = name;
    this.description = description;
    this.qName = qName;
    this.accesses = accesses;
  }

  public String getGuid() {
    return guid;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getqName() {
    return qName;
  }

  public Collection<JsonSecurityAccess> getAccesses() {
    return accesses;
  }

  /**
   * Построитель SecurityPackage
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель SecurityPackage
   */
  public static final class Builder {
    private String guid;
    private String code;
    private String name;
    private String description;
    private String qName;
    private Collection<JsonSecurityAccess> accesses;

    private Builder() {

    }

    /**
     * Создаем SecurityPackage
     *
     * @return SecurityPackage
     */
    public JsonSecurityPackage build() {
      return new JsonSecurityPackage(guid, code, name, description, qName, accesses);
    }

    /**
     * Гуид пакета
     *
     * @param guid Гуид пакета
     * @return Builder
     */
    public Builder guid(String guid) {
      this.guid = guid;
      return this;
    }

    /**
     * Кодовое имя пакета
     *
     * @param code Кодовое имя пакета
     * @return Builder
     */
    public Builder code(String code) {
      this.code = code;
      return this;
    }

    /**
     * Название пакета
     *
     * @param name Название пакета
     * @return Builder
     */
    public Builder name(String name) {
      this.name = name;
      return this;
    }

    /**
     * Описание пакета
     *
     * @param description Описание пакета
     * @return Builder
     */
    public Builder description(String description) {
      this.description = description;
      return this;
    }

    /**
     * QName пакета
     *
     * @param qName QName пакета
     * @return Builder
     */
    public Builder qName(String qName) {
      this.qName = qName;
      return this;
    }

    /**
     * Настройки доступа
     *
     * @param accesses Настройки доступа
     * @return Builder
     */
    public Builder accesses(Collection<JsonSecurityAccess> accesses) {
      this.accesses = accesses;
      return this;
    }
  }
}
