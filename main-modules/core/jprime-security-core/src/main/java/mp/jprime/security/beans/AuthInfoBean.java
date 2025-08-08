package mp.jprime.security.beans;

import mp.jprime.security.AuthInfo;
import mp.jprime.security.AuthBaseParams;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Данные авторизации
 */
public class AuthInfoBean extends AuthBaseParams implements AuthInfo {
  private final String username;
  private final String fio;
  private final Collection<String> roles;
  private final String userId;
  private final String userGuid;

  private final String oktmo;
  private final Collection<String> oktmoList;
  private final Collection<Integer> subjectGroups;

  private final String orgId;
  private final String sepDepId;
  private final String depId;
  private final String emplId;

  private final Esia esia;

  private final String token;
  private final String userIP;

  private AuthInfoBean(String userIP, String userId, String userGuid,
                       String oktmo, Collection<String> oktmoList,
                       Collection<Integer> subjectGroups,
                       String orgId, String sepDepId, String depId, String emplId,
                       String username, String fio,
                       Collection<String> roles, Esia esia, String token) {
    this.userIP = userIP;
    this.userId = userId;
    this.userGuid = userGuid;
    this.oktmo = oktmo;
    this.oktmoList = oktmoList == null || oktmoList.isEmpty() ? Collections.emptySet() : Set.copyOf(oktmoList);
    this.subjectGroups = subjectGroups == null || subjectGroups.isEmpty() ? Collections.emptySet() : Set.copyOf(subjectGroups);
    this.orgId = orgId;
    this.sepDepId = sepDepId;
    this.depId = depId;
    this.emplId = emplId;
    this.username = username;
    this.fio = fio;
    this.roles = roles == null || roles.isEmpty() ? Collections.emptySet() : Set.copyOf(roles);
    this.esia = esia;
    this.token = token;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public String getFio() {
    return fio;
  }

  @Override
  public Collection<String> getRoles() {
    return roles;
  }

  @Override
  public String getUserId() {
    return userId;
  }

  @Override
  public String getUserGuid() {
    return userGuid;
  }

  @Override
  public String getOktmo() {
    return oktmo;
  }

  @Override
  public Collection<String> getOktmoList() {
    return oktmoList;
  }

  @Override
  public Collection<Integer> getSubjectGroups() {
    return subjectGroups;
  }

  @Override
  public String getOrgId() {
    return orgId;
  }

  @Override
  public String getSepDepId() {
    return sepDepId;
  }

  @Override
  public String getDepId() {
    return depId;
  }

  @Override
  public String getEmplId() {
    return emplId;
  }

  @Override
  public Esia getEsia() {
    return esia;
  }

  @Override
  public String getUserIP() {
    return userIP;
  }

  @Override
  public String getToken() {
    return token;
  }

  /**
   * Построитель AuthenticationInfo
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель AuthInfo
   */
  public static final class Builder {
    private String userIP;
    private String userId;
    private String userGuid;
    private String oktmo;
    private Collection<String> oktmoList;
    private Collection<Integer> subjectGroups;
    private String orgId;
    private String sepDepId;
    private String depId;
    private String emplId;
    private String username;
    private String fio;
    private Collection<String> roles;
    private Esia esia;
    private String token;

    private Builder() {
    }

    /**
     * Создаем AuthInfoImpl
     *
     * @return AuthInfoImpl
     */
    public AuthInfoBean build() {
      return new AuthInfoBean(userIP, userId, userGuid, oktmo, oktmoList, subjectGroups,
          orgId, sepDepId, depId, emplId, username, fio, roles, esia, token);
    }

    /**
     * Идентификатор пользователя
     *
     * @param userId Идентификатор пользователя
     * @return Builder
     */
    public Builder userId(String userId) {
      this.userId = userId;
      return this;
    }

    /**
     * Глобальный идентификатор пользователя
     *
     * @param userGuid Глобальный идентификатор пользователя
     * @return Builder
     */
    public Builder userGuid(String userGuid) {
      this.userGuid = userGuid;
      return this;
    }

    /**
     * Основное ОКТМО пользователя
     *
     * @param oktmo Основное ОКТМО пользователя
     * @return Builder
     */
    public Builder oktmo(String oktmo) {
      this.oktmo = oktmo;
      return this;
    }

    /**
     * ОКТМО пользователя
     *
     * @param oktmoList ОКТМО пользователя
     * @return Builder
     */
    public Builder oktmoList(Collection<String> oktmoList) {
      this.oktmoList = oktmoList;
      return this;
    }

    /**
     * предметные группы пользователя
     *
     * @param subjectGroups предметные группы пользователя
     * @return Builder
     */
    public Builder subjectGroups(Collection<Integer> subjectGroups) {
      this.subjectGroups = subjectGroups;
      return this;
    }

    /**
     * Идентификатор организации пользователя
     *
     * @param orgId Идентификатор организации пользователя
     * @return Builder
     */
    public Builder orgId(String orgId) {
      this.orgId = orgId;
      return this;
    }

    /**
     * Идентификатор обособленного подразделения пользователя
     *
     * @param sepDepId Идентификатор обособленного подразделения пользователя
     * @return Builder
     */
    public Builder sepDepId(String sepDepId) {
      this.sepDepId = sepDepId;
      return this;
    }

    /**
     * Идентификатор подразделения пользователя
     *
     * @param depId Идентификатор подразделения пользователя
     * @return Builder
     */
    public Builder depId(String depId) {
      this.depId = depId;
      return this;
    }

    /**
     * Идентификатор сотрудника пользователя
     *
     * @param emplId Идентификатор сотрудника пользователя
     * @return Builder
     */
    public Builder emplId(String emplId) {
      this.emplId = emplId;
      return this;
    }

    /**
     * IP пользователя
     *
     * @param userIP IP пользователя
     * @return Builder
     */
    public Builder userIP(String userIP) {
      this.userIP = userIP;
      return this;
    }

    /**
     * Логин пользователя
     *
     * @param username Логин пользователя
     * @return Builder
     */
    public Builder username(String username) {
      this.username = username;
      return this;
    }

    /**
     * ФИО пользователя
     *
     * @param fio ФИО пользователя
     * @return Builder
     */
    public Builder fio(String fio) {
      this.fio = fio;
      return this;
    }

    /**
     * Роли пользователя
     *
     * @param roles Роли пользователя
     * @return Builder
     */
    public Builder roles(Collection<String> roles) {
      this.roles = roles;
      return this;
    }

    /**
     * Данные ЕСИА
     *
     * @param esia Данные ЕСИА
     * @return Builder
     */
    public Builder esia(Esia esia) {
      this.esia = esia;
      return this;
    }


    /**
     * Токен
     *
     * @param token Токен
     * @return Builder
     */
    public Builder token(String token) {
      this.token = token;
      return this;
    }
  }
}
