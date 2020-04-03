package mp.jprime.security.beans;

import mp.jprime.security.JPImmutableSecurityPackage;
import mp.jprime.security.JPSecurityPackage;

import java.util.Collection;

/**
 * Настройки безопасности пакета
 */
public final class JPImmutableSecurityPackageBean implements JPImmutableSecurityPackage {
  /**
   * пакет
   */
  private final JPSecurityPackage secPackage;

  private JPImmutableSecurityPackageBean(JPSecurityPackage secPackage) {
    this.secPackage = secPackage;
  }

  /**
   * Код пакета
   *
   * @return Код пакета
   */
  @Override
  public String getCode() {
    return secPackage.getCode();
  }

  /**
   * Описание пакета
   *
   * @return Описание пакета
   */
  @Override
  public String getDescription() {
    return secPackage.getDescription();
  }

  /**
   * Название пакета
   *
   * @return Название пакета
   */
  @Override
  public String getName() {
    return secPackage.getName();
  }

  /**
   * QName пакета
   *
   * @return qName пакета
   */
  @Override
  public String getQName() {
    return secPackage.getQName();
  }

  /**
   * Разрешительные настройки
   *
   * @return Разрешительные настройки
   */
  @Override
  public Collection<JPAccess> getPermitAccess() {
    return secPackage.getPermitAccess();
  }

  /**
   * Запретительные настройки
   *
   * @return Запретительные настройки
   */
  @Override
  public Collection<JPAccess> getProhibitionAccess() {
    return secPackage.getProhibitionAccess();
  }

  /**
   * Проверка доступа на чтение
   *
   * @param roles Роли
   * @return Да/Нет
   */
  @Override
  public boolean checkRead(Collection<String> roles) {
    return secPackage.checkRead(roles);
  }

  /**
   * Проверка доступа на удаление
   *
   * @param roles Роли
   * @return Да/Нет
   */
  @Override
  public boolean checkDelete(Collection<String> roles) {
    return secPackage.checkDelete(roles);
  }

  /**
   * Проверка доступа на обновление
   *
   * @param roles Роли
   * @return Да/Нет
   */
  @Override
  public boolean checkUpdate(Collection<String> roles) {
    return secPackage.checkUpdate(roles);
  }

  /**
   * Проверка доступа на создание
   *
   * @param roles Роли
   * @return Да/Нет
   */
  @Override
  public boolean checkCreate(Collection<String> roles) {
    return secPackage.checkCreate(roles);
  }

  @Override
  public String toString() {
    return secPackage.toString();
  }

  /**
   * Построитель JPImmutableSecurityPackageBean
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JPImmutableSecurityPackageBean
   */
  public static final class Builder {
    private JPSecurityPackage secPackage;

    private Builder() {
    }

    /**
     * Создаем JPImmutableSecurityPackageBean
     *
     * @return JPImmutableSecurityPackageBean
     */
    public JPImmutableSecurityPackageBean build() {
      return new JPImmutableSecurityPackageBean(secPackage);
    }

    /**
     * пакет
     *
     * @param secPackage пакет
     * @return Builder
     */
    public Builder secPackage(JPSecurityPackage secPackage) {
      this.secPackage = secPackage;
      return this;
    }
  }
}
