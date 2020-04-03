package mp.jprime.security.beans;

import mp.jprime.security.ConnectionInfo;

/**
 * Информация о соединении
 */
public class ConnectionInfoImpl implements ConnectionInfo {
  private final String userIP;
  private final String username;

  /**
   * Конструктор
   *
   * @param userIP   IP пользователя
   * @param username Имя пользователя
   */
  private ConnectionInfoImpl(String userIP, String username) {
    this.userIP = userIP;
    this.username = username;
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
   * Возвращает IP пользователя
   *
   * @return IP пользователя
   */
  @Override
  public String getUserIP() {
    return userIP;
  }

  /**
   * Построитель ConnectionInfo
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель ConnectionInfo
   */
  public static final class Builder {
    private String userIP;
    private String username;

    private Builder() {
    }

    /**
     * Создаем JPSelect
     *
     * @return JPSelect
     */
    public ConnectionInfoImpl build() {
      return new ConnectionInfoImpl(userIP, username);
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

  }
}
