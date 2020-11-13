package mp.jprime.security.json.beans;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Описание настроек доступа
 */
@JsonPropertyOrder({
    "guid",
    "type",
    "role",
    "read",
    "create",
    "update",
    "delete"
})
public class JsonSecurityAccess {
  private String guid;
  private String type;
  private String role;
  private boolean read;
  private boolean create;
  private boolean update;
  private boolean delete;

  public JsonSecurityAccess() {

  }

  private JsonSecurityAccess(String guid, String type, String role,
                             boolean read, boolean create, boolean update, boolean delete) {
    this.guid = guid;
    this.type = type;
    this.role = role;
    this.read = read;
    this.create = create;
    this.update = update;
    this.delete = delete;
  }

  public String getGuid() {
    return guid;
  }

  public String getType() {
    return type;
  }

  public String getRole() {
    return role;
  }

  public boolean isRead() {
    return read;
  }

  public boolean isCreate() {
    return create;
  }

  public boolean isUpdate() {
    return update;
  }

  public boolean isDelete() {
    return delete;
  }

  /**
   * Построитель SecurityAccess
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель SecurityAccess
   */
  public static final class Builder {
    private String guid;
    private String type;
    private String role;
    private boolean read;
    private boolean create;
    private boolean update;
    private boolean delete;

    private Builder() {

    }

    /**
     * Создаем SecurityPackage
     *
     * @return SecurityPackage
     */
    public JsonSecurityAccess build() {
      return new JsonSecurityAccess(guid, type, role, read, create, update, delete);
    }

    /**
     * Гуид доступа
     *
     * @param guid Гуид доступа
     * @return Builder
     */
    public Builder guid(String guid) {
      this.guid = guid;
      return this;
    }

    /**
     * Тип доступа
     *
     * @param type Тип доступа
     * @return Builder
     */
    public Builder type(String type) {
      this.type = type;
      return this;
    }

    /**
     * Имя роли
     *
     * @param role Имя роли
     * @return Builder
     */
    public Builder role(String role) {
      this.role = role;
      return this;
    }

    public Builder read(boolean read) {
      this.read = read;
      return this;
    }

    public Builder create(boolean create) {
      this.create = create;
      return this;
    }

    public Builder update(boolean update) {
      this.update = update;
      return this;
    }

    public Builder delete(boolean delete) {
      this.delete = delete;
      return this;
    }
  }
}
