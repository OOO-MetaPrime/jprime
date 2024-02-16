package mp.jprime.security.beans;

import mp.jprime.security.AuthInfo;

import java.util.Collection;
import java.util.Collections;

/**
 * Данные авторизации
 */
public class AuthInfoBean implements AuthInfo {
  private final String userIP;
  private final String userId;
  private final String oktmo;
  private final String administration;
  private final Collection<Integer> subjectGroups;
  private final String username;
  private final String fio;
  private final Collection<String> roles;
  private final String orgId;
  private final String depId;
  private final String token;

  /**
   * Конструктор
   *
   * @param userIP         IP пользователя
   * @param userId         Идентификатор пользователя
   * @param oktmo          ОКТМО пользователя
   * @param administration ведомство пользователя
   * @param subjectGroups  предметные группы пользователя
   * @param orgId          Идентификатор организации пользователя
   * @param depId          Идентификатор подразделения пользователя
   * @param username       Логин пользователя
   * @param fio            ФИО пользователя
   * @param roles          Роли пользователя
   * @param token          Токен
   */
  private AuthInfoBean(String userIP, String userId,
                       String oktmo, String administration, Collection<Integer> subjectGroups,
                       String orgId, String depId, String username, String fio,
                       Collection<String> roles, String token) {
    this.userIP = userIP;
    this.userId = userId;
    this.oktmo = oktmo;
    this.administration = administration;
    this.subjectGroups = subjectGroups == null ? Collections.emptyList() : Collections.unmodifiableCollection(subjectGroups);
    this.orgId = orgId;
    this.depId = depId;
    this.username = username;
    this.fio = fio;
    this.roles = roles == null ? Collections.emptyList() : Collections.unmodifiableCollection(roles);
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
  public String getOktmo() {
    return oktmo;
  }

  @Override
  public String getAdministration() {
    return administration;
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
  public String getDepId() {
    return depId;
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
    private String oktmo;
    private String administration;
    private Collection<Integer> subjectGroups;
    private String orgId;
    private String depId;
    private String username;
    private String fio;
    private Collection<String> roles;
    private String token;

    private Builder() {
    }

    /**
     * Создаем AuthInfoImpl
     *
     * @return AuthInfoImpl
     */
    public AuthInfoBean build() {
      return new AuthInfoBean(userIP, userId, oktmo, administration, subjectGroups, orgId, depId, username, fio, roles, token);
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
     * ОКТМО пользователя
     *
     * @param oktmo ОКТМО пользователя
     * @return Builder
     */
    public Builder oktmo(String oktmo) {
      this.oktmo = oktmo;
      return this;
    }

    /**
     * ведомство пользователя
     *
     * @param administration ведомство пользователя
     * @return Builder
     */
    public Builder administration(String administration) {
      this.administration = administration;
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
