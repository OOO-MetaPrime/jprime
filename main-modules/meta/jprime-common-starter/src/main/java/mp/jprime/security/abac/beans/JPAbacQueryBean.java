package mp.jprime.security.abac.beans;

import mp.jprime.security.abac.JPAbacQuery;

import java.util.Collection;
import java.util.Collections;

/**
 * Запрос для поиска настроек политик безопасности
 */
public final class JPAbacQueryBean implements JPAbacQuery {
  private final String name;
  private final Collection<String> roles;
  private final Collection<String> jpClassCodes;
  private final boolean useCreate;
  private final boolean useRead;
  private final boolean useUpdate;
  private final boolean useDelete;
  private final boolean usePermit;
  private final boolean useProhibition;
  private final boolean useEnviromentRules;

  private JPAbacQueryBean(String name, Collection<String> roles, Collection<String> jpClassCodes,
                          boolean useCreate, boolean useRead, boolean useUpdate, boolean useDelete,
                          boolean usePermit, boolean useProhibition,
                          boolean useEnviromentRules) {
    this.name = name;
    this.roles = Collections.unmodifiableCollection(roles != null ? roles : Collections.emptyList());
    this.jpClassCodes = Collections.unmodifiableCollection(jpClassCodes != null ? jpClassCodes : Collections.emptyList());
    this.useCreate = useCreate;
    this.useRead = useRead;
    this.useUpdate = useUpdate;
    this.useDelete = useDelete;
    this.usePermit = usePermit;
    this.useProhibition = useProhibition;
    this.useEnviromentRules = useEnviromentRules;
  }

  /**
   * Имя для фильтрации
   *
   * @return Значение для поиска по имени
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Список ролей для фильтрации
   *
   * @return Список ролей
   */
  @Override
  public Collection<String> getRoles() {
    return roles;
  }

  /**
   * Список классов для фильтрации
   *
   * @return Список классов
   */
  @Override
  public Collection<String> getJpClassCodes() {
    return jpClassCodes;
  }

  /**
   * Поиск по созданию
   *
   * @return Поиск по созданию
   */
  @Override
  public boolean useCreate() {
    return useCreate;
  }

  /**
   * Поиск по чтению
   *
   * @return Поиск по чтению
   */
  @Override
  public boolean useRead() {
    return useRead;
  }

  /**
   * Поиск по изменению
   *
   * @return Поиск по изменению
   */
  @Override
  public boolean useUpdate() {
    return useUpdate;
  }

  /**
   * Поиск по удалению
   *
   * @return Поиск по удалению
   */
  @Override
  public boolean useDelete() {
    return useDelete;
  }

  /**
   * Поиск по настройкам на разрешение
   *
   * @return Поиск по настройкам на разрешение
   */
  @Override
  public boolean usePermit() {
    return usePermit;
  }

  /**
   * Поиск по настройкам на запрет
   *
   * @return Поиск по настройкам на запрет
   */
  @Override
  public boolean useProhibition() {
    return useProhibition;
  }

  /**
   * Поиск по наличию правил окружения
   *
   * @return Поиск по наличию правил окружения
   */
  @Override
  public boolean useEnviromentRules() {
    return useEnviromentRules;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private String name;
    private Collection<String> roles;
    private Collection<String> jpClassCodes;
    private boolean useCreate;
    private boolean useRead;
    private boolean useUpdate;
    private boolean useDelete;
    private boolean usePermit;
    private boolean useProhibition;
    private boolean useEnviromentRules;

    private Builder() {

    }

    public JPAbacQueryBean build() {
      return new JPAbacQueryBean(name, roles, jpClassCodes,
          useCreate, useRead, useUpdate, useDelete,
          usePermit, useProhibition,
          useEnviromentRules);
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder roles(Collection<String> roles) {
      this.roles = roles;
      return this;
    }

    public Builder jpClassCodes(Collection<String> jpClassCodes) {
      this.jpClassCodes = jpClassCodes;
      return this;
    }

    public Builder useCreate(boolean useCreate) {
      this.useCreate = useCreate;
      return this;
    }

    public Builder useRead(boolean useRead) {
      this.useRead = useRead;
      return this;
    }

    public Builder useUpdate(boolean useUpdate) {
      this.useUpdate = useUpdate;
      return this;
    }

    public Builder useDelete(boolean useDelete) {
      this.useDelete = useDelete;
      return this;
    }

    public Builder usePermit(boolean usePermit) {
      this.usePermit = usePermit;
      return this;
    }

    public Builder useProhibition(boolean useProhibition) {
      this.useProhibition = useProhibition;
      return this;
    }

    public Builder useEnviromentRules(boolean useEnviromentRules) {
      this.useEnviromentRules = useEnviromentRules;
      return this;
    }
  }
}
