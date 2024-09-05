package mp.jprime.security.beans;

import mp.jprime.security.AuthInfo;
import mp.jprime.security.AuthBaseParams;
import mp.jprime.security.jwt.JWTInfo;

import java.util.Collection;

/**
 * Данные авторизации
 */
public class AuthInfoJWTWrapperBean extends AuthBaseParams implements AuthInfo {
  private final String userIP;
  private final JWTInfo jwtInfo;

  /**
   * Конструктор
   *
   * @param userIP  IP пользователя
   * @param jwtInfo JWTInfo
   */
  private AuthInfoJWTWrapperBean(String userIP, JWTInfo jwtInfo) {
    this.userIP = userIP;
    this.jwtInfo = jwtInfo;
  }

  /**
   * Конструктор
   *
   * @param userIP  IP пользователя
   * @param jwtInfo JWTInfo
   */
  public static AuthInfo of(String userIP, JWTInfo jwtInfo) {
    return new AuthInfoJWTWrapperBean(userIP, jwtInfo);
  }

  @Override
  public String getUsername() {
    return jwtInfo.getUsername();
  }

  public String getFio() {
    return jwtInfo.getFio();
  }

  @Override
  public Collection<String> getRoles() {
    return jwtInfo.getRoles();
  }

  @Override
  public String getUserId() {
    return jwtInfo.getUserId();
  }

  public String getUserGuid() {
    return jwtInfo.getUserGuid();
  }

  @Override
  public String getOktmo() {
    return jwtInfo.getOktmo();
  }


  @Override
  public Collection<String> getOktmoList() {
    return jwtInfo.getOktmoList();
  }

  @Override
  public Collection<String> getOktmoPrefixList() {
    return jwtInfo.getOktmoPrefixList();
  }

  @Override
  public Collection<String> getOktmoTreeList() {
    return jwtInfo.getOktmoTreeList();
  }

  @Override
  public Collection<Integer> getSubjectGroups() {
    return jwtInfo.getSubjectGroups();
  }

  @Override
  public String getOrgId() {
    return jwtInfo.getOrgId();
  }

  @Override
  public String getSepDepId() {
    return jwtInfo.getSepDepId();
  }

  @Override
  public String getDepId() {
    return jwtInfo.getDepId();
  }

  @Override
  public String getUserIP() {
    return userIP;
  }

  @Override
  public String getToken() {
    return jwtInfo.getToken();
  }
}
