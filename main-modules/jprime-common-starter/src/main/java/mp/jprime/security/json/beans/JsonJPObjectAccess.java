package mp.jprime.security.json.beans;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Доступ к объекту текущим пользователем
 */
@JsonPropertyOrder({
    "objectClassCode",
    "objectId",
    "read",
    "create",
    "update",
    "delete"
})
public final class JsonJPObjectAccess {
  private String objectClassCode;
  private String objectId;
  private boolean read;
  private boolean create;
  private boolean update;
  private boolean delete;

  public JsonJPObjectAccess() {

  }

  private JsonJPObjectAccess(String objectClassCode, String objectId,
                             boolean read, boolean create, boolean update, boolean delete) {
    this.objectClassCode = objectClassCode;
    this.objectId = objectId;
    this.read = read;
    this.create = create;
    this.update = update;
    this.delete = delete;
  }

  public String getObjectClassCode() {
    return objectClassCode;
  }

  public String getObjectId() {
    return objectId;
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
   * Построитель JPClassAccess
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JPClassAccess
   */
  public static final class Builder {
    private String objectClassCode;
    private String objectId;
    private boolean read;
    private boolean create;
    private boolean update;
    private boolean delete;

    private Builder() {

    }

    /**
     * Создаем JPClassAccess
     *
     * @return JPClassAccess
     */
    public JsonJPObjectAccess build() {
      return new JsonJPObjectAccess(objectClassCode, objectId, read, create, update, delete);
    }

    /**
     * Кодовое имя класса объекта
     *
     * @param objectClassCode Кодовое имя класса объекта
     * @return Builder
     */
    public Builder objectClassCode(String objectClassCode) {
      this.objectClassCode = objectClassCode;
      return this;
    }

    /**
     * Идентификатор объекта
     *
     * @param objectId Идентификатор объекта
     * @return Builder
     */
    public Builder objectId(String objectId) {
      this.objectId = objectId;
      return this;
    }

    /**
     * Доступ на чтение
     *
     * @param read Доступ на чтение
     * @return Builder
     */
    public Builder read(boolean read) {
      this.read = read;
      return this;
    }

    /**
     * Доступ на создание
     *
     * @param create Доступ на создание
     * @return Builder
     */
    public Builder create(boolean create) {
      this.create = create;
      return this;
    }

    /**
     * Доступ на изменение
     *
     * @param update Доступ на изменение
     * @return Builder
     */
    public Builder update(boolean update) {
      this.update = update;
      return this;
    }

    /**
     * Доступ на удаление
     *
     * @param delete Доступ на удаление
     * @return Builder
     */
    public Builder delete(boolean delete) {
      this.delete = delete;
      return this;
    }
  }
}
