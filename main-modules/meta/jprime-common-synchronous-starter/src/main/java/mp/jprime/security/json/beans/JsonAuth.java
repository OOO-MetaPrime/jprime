package mp.jprime.security.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;

/**
 * Модель данных авторизации
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonAuth {
  private String userIP;
  private String userId;
  private String orgId;
  private String depId;
  private String username;
  private Collection<String> roles;

  public String getUserIP() {
    return userIP;
  }

  public void setUserIP(String userIP) {
    this.userIP = userIP;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getOrgId() {
    return orgId;
  }

  public void setOrgId(String orgId) {
    this.orgId = orgId;
  }

  public String getDepId() {
    return depId;
  }

  public void setDepId(String depId) {
    this.depId = depId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Collection<String> getRoles() {
    return roles;
  }

  public void setRoles(Collection<String> roles) {
    this.roles = roles;
  }
}
