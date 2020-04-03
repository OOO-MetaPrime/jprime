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
  private final String username;
  private final Collection<String> roles;
  private final String orgId;
  private final String depId;
  private final String token;

  /**
   * Конструктор
   *
   * @param userIP   IP пользователя
   * @param userId   Идентификатор пользователя
   * @param orgId    Идентификатор организации пользователя
   * @param depId    Идентификатор подразделения пользователя
   * @param username Имя пользователя
   * @param roles    Роли пользователя
   * @param token    Токен
   */
  private AuthInfoBean(String userIP, String userId, String orgId, String depId, String username,
                       Collection<String> roles, String token) {
    this.userIP = userIP;
    this.userId = userId;
    this.orgId = orgId;
    this.depId = depId;
    this.username = username;
    this.roles = Collections.unmodifiableCollection(roles == null ? Collections.emptyList() : roles);
    this.token = token;
  }

  /**
   * Возвращает имя пользователя
   *
   * @return Имя пользователя
   */
  @Override
  public String getUsername() {
    return username;
  }

  /**
   * Возвращает роли пользователя
   *
   * @return Роли
   */
  @Override
  public Collection<String> getRoles() {
    return roles;
  }

  /**
   * Возвращает идентификатор пользователя
   *
   * @return идентификатор пользователя
   */
  @Override
  public String getUserId() {
    return userId;
  }

  /**
   * Возвращает идентификатор организации пользователя
   *
   * @return идентификатор организации пользователя
   */
  @Override
  public String getOrgId() {
    return orgId;
  }

  /**
   * Возвращает идентификатор подразделения пользователя
   *
   * @return идентификатор подразделения пользователя
   */
  @Override
  public String getDepId() {
    return depId;
  }

  /**
   * Возвращает IP пользователя
   *
   * @return IP пользователя
   */
  @Override
  public String getUserIP() {
    return userIP;
  }

  /**
   * Возвращает полностью токен
   *
   * @return Токен
   */
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
    private String orgId;
    private String depId;
    private String username;
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
      return new AuthInfoBean(userIP, userId, orgId, depId, username, roles, token);
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
     * Имя пользователя
     *
     * @param username Имя пользователя
     * @return Builder
     */
    public Builder username(String username) {
      this.username = username;
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
