package mp.jprime.security.beans;

import mp.jprime.security.JPSecurityPackageAccess;

/**
 * Настройка уровня доступа
 */
public class JPSecurityPackageAccessBean implements JPSecurityPackageAccess {
  private final boolean read;
  private final boolean create;
  private final boolean update;
  private final boolean delete;
  private final String role;

  /**
   * Настройки уровней доступа
   *
   * @param read   Доступ на чтение
   * @param create Доступ на создание
   * @param update Доступ на изменение
   * @param delete Доступ на удаление
   * @param role   Роль
   */
  private JPSecurityPackageAccessBean(boolean read, boolean create, boolean update, boolean delete, String role) {
    this.read = read;
    this.create = create;
    this.update = update;
    this.delete = delete;
    this.role = role;
  }

  /**
   * Доступ на чтение
   *
   * @return Да/Нет
   */
  public boolean isRead() {
    return read;
  }

  /**
   * Доступ на создание
   *
   * @return Да/Нет
   */
  public boolean isCreate() {
    return create;
  }

  /**
   * Доступ на изменение
   *
   * @return Да/Нет
   */
  public boolean isUpdate() {
    return update;
  }

  /**
   * Доступ на удаление
   *
   * @return Да/Нет
   */
  public boolean isDelete() {
    return delete;
  }

  /**
   * Роль
   *
   * @return Роль
   */
  public String getRole() {
    return role;
  }

  /**
   * Построитель JPAccess
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JPAccess
   */
  public static final class Builder {
    private boolean read;
    private boolean create;
    private boolean update;
    private boolean delete;
    private String role;

    private Builder() {
    }

    /**
     * Создаем JPAccess
     *
     * @return JPAccess
     */
    public JPSecurityPackageAccessBean build() {
      return new JPSecurityPackageAccessBean(read, create, update, delete, role);
    }

    /**
     * Доступ на чтение
     *
     * @param read Да/Нет
     * @return Builder
     */
    public Builder read(boolean read) {
      this.read = read;
      return this;
    }


    /**
     * Доступ на создание
     *
     * @param create Да/Нет
     * @return Builder
     */
    public Builder create(boolean create) {
      this.create = create;
      return this;
    }

    /**
     * Доступ на изменение
     *
     * @param update Да/Нет
     * @return Builder
     */
    public Builder update(boolean update) {
      this.update = update;
      return this;
    }

    /**
     * Доступ на удаление
     *
     * @param delete Да/Нет
     * @return Builder
     */
    public Builder delete(boolean delete) {
      this.delete = delete;
      return this;
    }

    /**
     * Код роли
     *
     * @param role Код роли
     * @return Builder
     */
    public Builder role(String role) {
      this.role = role;
      return this;
    }
  }
}
