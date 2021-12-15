package mp.jprime.security.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;


/**
 * Доступ к объекту текущим пользователем
 */
@JsonPropertyOrder({
    "objectClassCode",
    "objectId",
    "read",
    "create",
    "update",
    "delete",
    "editAttrs",
})
@JsonIgnoreProperties(ignoreUnknown = true)
public final class JsonJPObjectAccess {
  private String objectClassCode;
  private String objectId;
  private boolean read;
  private boolean create;
  private boolean update;
  private boolean delete;
  private Map<String, Boolean> editAttrs = new HashMap<>();

  public JsonJPObjectAccess() {

  }

  private JsonJPObjectAccess(String objectClassCode, String objectId,
                             boolean read, boolean create, boolean update, boolean delete,
                             Map<String, Boolean> editAttrs) {
    this.objectClassCode = objectClassCode;
    this.objectId = objectId;
    this.read = read;
    this.create = create;
    this.update = update;
    this.delete = delete;
    if (editAttrs != null) {
      this.editAttrs.putAll(editAttrs);
    }
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

  public Map<String, Boolean> getEditAttrs() {
    return editAttrs;
  }

  /**
   * Построитель {@link JsonJPObjectAccess}
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель {@link JsonJPObjectAccess}
   */
  public static final class Builder {
    private String objectClassCode;
    private String objectId;
    private boolean read;
    private boolean create;
    private boolean update;
    private boolean delete;
    private Map<String, Boolean> editAttrs = new HashMap<>();

    private Builder() {

    }

    /**
     * Создаем {@link JsonJPObjectAccess}
     *
     * @return {@link JsonJPObjectAccess}
     */
    public JsonJPObjectAccess build() {
      return new JsonJPObjectAccess(objectClassCode, objectId, read, create, update, delete, editAttrs);
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

    /**
     * Настройки доступа к атрибуту
     *
     * @param attrCode Кодовое имя атрибута
     * @param isView   Доступ на чтение
     * @param isEdit   Доступ на редактирвание
     * @return Builder
     */
    public Builder attrAccess(String attrCode, boolean isView, boolean isEdit) {
      if (isView) {
        editAttrs.put(attrCode, isEdit);
      } else {
        editAttrs.remove(attrCode);
      }
      return this;
    }

    /**
     * Настройки доступа к атрибуту
     *
     * @param attrCode Кодовое имя атрибута
     * @param isEdit   Доступ на редактирвание
     * @return Builder
     */
    public Builder attrEdit(String attrCode, boolean isEdit) {
      return attrAccess(attrCode, Boolean.TRUE, isEdit);
    }
  }
}
