package mp.jprime.security.beans;

import mp.jprime.security.JPSecurityPackageAccess;
import mp.jprime.security.JPSecurityPackage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Настройки безопасности пакета
 */
public class JPSecurityPackageBean implements JPSecurityPackage {
  /**
   * Код пакета
   */
  private final String code;
  /**
   * Название пакета
   */
  private final String name;
  /**
   * Описание пакета
   */
  private final String description;
  /**
   * QName пакета
   */
  private final String qName;
  /**
   * Разрешительные настройки
   */
  private final Map<String, JPSecurityPackageAccess> permitAccess;
  /**
   * Запретительные настройки
   */
  private final Map<String, JPSecurityPackageAccess> prohibitionAccess;
  private final Boolean immutable;

  private JPSecurityPackageBean(String code, String description, String name, String qName,
                                Collection<JPSecurityPackageAccess> permitAccess, Collection<JPSecurityPackageAccess> prohibitionAccess,
                                boolean immutable) {
    this.immutable = immutable;

    this.code = code;
    this.description = description;
    this.name = name;
    this.qName = qName;

    final Map<String, JPSecurityPackageAccess> newPermitAccess = new HashMap<>();
    if (permitAccess != null) {
      permitAccess.forEach(x -> newPermitAccess.put(x.getRole(), x));
    }
    this.permitAccess = newPermitAccess;

    final Map<String, JPSecurityPackageAccess> newProhibitionAccess = new HashMap<>();
    if (prohibitionAccess != null) {
      prohibitionAccess.forEach(x -> newProhibitionAccess.put(x.getRole(), x));
    }
    this.prohibitionAccess = newProhibitionAccess;
  }

  /**
   * Код пакета
   *
   * @return Код пакета
   */
  @Override
  public String getCode() {
    return code;
  }

  /**
   * Название пакета
   *
   * @return Название пакета
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Описание пакета
   *
   * @return Описание пакета
   */
  @Override
  public String getDescription() {
    return description;
  }

  /**
   * QName пакета
   *
   * @return qName пакета
   */
  @Override
  public String getQName() {
    return qName;
  }

  /**
   * Разрешительные настройки
   *
   * @return Разрешительные настройки
   */
  @Override
  public Collection<JPSecurityPackageAccess> getPermitAccess() {
    return permitAccess.values();
  }

  /**
   * Запретительные настройки
   *
   * @return Запретительные настройки
   */
  @Override
  public Collection<JPSecurityPackageAccess> getProhibitionAccess() {
    return prohibitionAccess.values();
  }

  /**
   * Проверка доступа на чтение
   *
   * @param roles Роли
   * @return Да/Нет
   */
  @Override
  public boolean checkRead(Collection<String> roles) {
    boolean result = false;
    for (String role : roles) {
      JPSecurityPackageAccess prohibition = prohibitionAccess.get(role);
      if (prohibition != null && prohibition.isRead()) {
        return false;
      }
      JPSecurityPackageAccess permit = permitAccess.get(role);
      result = result || (permit != null && permit.isRead());
    }
    return result;
  }

  /**
   * Проверка доступа на удаление
   *
   * @param roles Роли
   * @return Да/Нет
   */
  @Override
  public boolean checkDelete(Collection<String> roles) {
    boolean result = false;
    for (String role : roles) {
      JPSecurityPackageAccess prohibition = prohibitionAccess.get(role);
      if (prohibition != null && prohibition.isDelete()) {
        return false;
      }
      JPSecurityPackageAccess permit = permitAccess.get(role);
      result = result || (permit != null && permit.isDelete());
    }
    return result;
  }

  /**
   * Проверка доступа на обновление
   *
   * @param roles Роли
   * @return Да/Нет
   */
  @Override
  public boolean checkUpdate(Collection<String> roles) {
    boolean result = false;
    for (String role : roles) {
      JPSecurityPackageAccess prohibition = prohibitionAccess.get(role);
      if (prohibition != null && prohibition.isUpdate()) {
        return false;
      }
      JPSecurityPackageAccess permit = permitAccess.get(role);
      result = result || (permit != null && permit.isUpdate());
    }
    return result;
  }

  /**
   * Проверка доступа на создание
   *
   * @param roles Роли
   * @return Да/Нет
   */
  @Override
  public boolean checkCreate(Collection<String> roles) {
    boolean result = false;
    for (String role : roles) {
      JPSecurityPackageAccess prohibition = prohibitionAccess.get(role);
      if (prohibition != null && prohibition.isCreate()) {
        return false;
      }
      JPSecurityPackageAccess permit = permitAccess.get(role);
      result = result || (permit != null && permit.isCreate());
    }
    return result;
  }

  /**
   * Признак неизменяемой настройки
   *
   * @return Да/Нет
   */
  @Override
  public boolean isImmutable() {
    return immutable;
  }

  @Override
  public String toString() {
    return "JPPackageSecurity{" +
        "code='" + code + '\'' +
        ", name='" + description + '\'' +
        ", qName='" + qName + '\'' +
        ", permitAccess='" + permitAccess + '\'' +
        ", prohibitionAccess='" + prohibitionAccess + '\'' +
        ", immutable=" + immutable +
        '}';
  }

  /**
   * Построитель JPPackageSecurity
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JPPackageSecurity
   */
  public static final class Builder {
    private String code;
    private String description;
    private String name;
    private String qName;
    private Collection<JPSecurityPackageAccess> permitAccess = new ArrayList<>();
    private Collection<JPSecurityPackageAccess> prohibitionAccess = new ArrayList<>();
    private boolean immutable = true;

    private Builder() {
    }

    /**
     * Создаем JPPackageSecurity
     *
     * @return JPPackageSecurity
     */
    public JPSecurityPackageBean build() {
      return new JPSecurityPackageBean(code, description, name, qName, permitAccess, prohibitionAccess, immutable);
    }

    /**
     * Код пакета
     *
     * @param code Код пакета
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
     * Настройки на разрешение
     *
     * @param access Настройки доступа
     * @return Builder
     */
    public Builder permitAccess(JPSecurityPackageAccess access) {
      this.permitAccess.add(access);
      return this;
    }

    /**
     * Настройка на запрет
     *
     * @param access Настройки доступа
     * @return Builder
     */
    public Builder prohibitionAccess(JPSecurityPackageAccess access) {
      this.prohibitionAccess.add(access);
      return this;
    }

    /**
     * Признак неизменяемости
     *
     * @param immutable Признак неизменяемости
     * @return Builder
     */
    public Builder immutable(boolean immutable) {
      this.immutable = immutable;
      return this;
    }
  }
}
