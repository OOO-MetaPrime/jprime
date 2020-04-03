package mp.jprime.security.json.beans;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Доступ к классу текущим пользователем
 */
@JsonPropertyOrder({
    "classCode",
    "read",
    "create",
    "update",
    "delete"
})
public final class JsonJPClassAccess {
  private String classCode;
  private boolean read;
  private boolean create;
  private boolean update;
  private boolean delete;

  public JsonJPClassAccess() {

  }

  private JsonJPClassAccess(String classCode, boolean read, boolean create, boolean update, boolean delete) {
    this.classCode = classCode;
    this.read = read;
    this.create = create;
    this.update = update;
    this.delete = delete;
  }

  public String getClassCode() {
    return classCode;
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
    private String classCode;
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
    public JsonJPClassAccess build() {
      return new JsonJPClassAccess(classCode, read, create, update, delete);
    }

    /**
     * Кодовое имя класса
     *
     * @param classCode Кодовое имя класса
     * @return Builder
     */
    public Builder classCode(String classCode) {
      this.classCode = classCode;
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
