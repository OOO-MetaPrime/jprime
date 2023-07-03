package mp.jprime.security.abac.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import mp.jprime.security.abac.JPAbacQuery;
import mp.jprime.security.abac.beans.JPAbacQueryBean;

import java.util.Collection;
import java.util.Collections;

/**
 * Запрос для поиска настроек политик безопасности
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class JsonAbacQuery {
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

  public JsonAbacQuery() {

  }

  private JsonAbacQuery(String name, Collection<String> roles, Collection<String> jpClassCodes,
                        boolean useCreate, boolean useRead, boolean useUpdate, boolean useDelete,
                        boolean usePermit, boolean useProhibition,
                        boolean useEnviromentRules) {
    this.name = name;
    this.roles = roles != null ? Collections.unmodifiableCollection(roles) : Collections.emptyList();
    this.jpClassCodes = jpClassCodes != null ? Collections.unmodifiableCollection(jpClassCodes) : Collections.emptyList();
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
  public String getName() {
    return name;
  }

  /**
   * Список ролей для фильтрации
   *
   * @return Список ролей
   */
  public Collection<String> getRoles() {
    return roles;
  }

  /**
   * Список классов для фильтрации
   *
   * @return Список классов
   */
  public Collection<String> getJpClassCodes() {
    return jpClassCodes;
  }

  /**
   * Поиск по созданию
   *
   * @return Поиск по созданию
   */
  public boolean isUseCreate() {
    return useCreate;
  }

  /**
   * Поиск по чтению
   *
   * @return Поиск по чтению
   */
  public boolean isUseRead() {
    return useRead;
  }

  /**
   * Поиск по изменению
   *
   * @return Поиск по изменению
   */
  public boolean isUseUpdate() {
    return useUpdate;
  }

  /**
   * Поиск по удалению
   *
   * @return Поиск по удалению
   */
  public boolean isUseDelete() {
    return useDelete;
  }

  /**
   * Поиск по настройкам на разрешение
   *
   * @return Поиск по настройкам на разрешение
   */
  public boolean isUsePermit() {
    return usePermit;
  }

  /**
   * Поиск по настройкам на запрет
   *
   * @return Поиск по настройкам на запрет
   */
  public boolean isUseProhibition() {
    return useProhibition;
  }

  /**
   * Поиск по наличию правил окружения
   *
   * @return Поиск по наличию правил окружения
   */
  public boolean isUseEnviromentRules() {
    return useEnviromentRules;
  }

  /**
   * Создание JPAbacQuery
   *
   * @return Новый JPAbacQuery
   */
  public JPAbacQuery toAbacQuery() {
    return JPAbacQueryBean.newBuilder()
        .name(this.getName())
        .roles(this.getRoles())
        .jpClassCodes(this.getJpClassCodes())
        .useCreate(this.isUseCreate())
        .useRead(this.isUseRead())
        .useUpdate(this.isUseUpdate())
        .useDelete(this.isUseDelete())
        .usePermit(this.isUsePermit())
        .useProhibition(this.isUseProhibition())
        .useEnviromentRules(this.isUseEnviromentRules())
        .build();
  }

  public static Builder newBuilder(String name) {
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

    public JsonAbacQuery build() {
      return new JsonAbacQuery(name, roles, jpClassCodes,
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
