package mp.jprime.security.services;

import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.services.JsonMapper;
import mp.jprime.security.AuthConvertor;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.beans.AuthInfoBean;
import mp.jprime.security.json.beans.JsonAuth;
import org.springframework.stereotype.Service;

/**
 * Обработчик аутентификации
 */
@Service
public class AuthServiceImpl implements JsonMapper, AuthConvertor<AuthInfo> {
  /**
   * Создает описание аутентификации
   *
   * @param json Строка
   * @return Описание аутентификации
   */
  @Override
  public AuthInfo getQuery(String json) {
    if (json == null) {
      return null;
    }
    try {
      JsonAuth a = OBJECT_MAPPER.readValue(json, JsonAuth.class);

      return AuthInfoBean.newBuilder()
          .userIP(a.getUserIP())
          .userId(a.getUserId())
          .orgId(a.getOrgId())
          .depId(a.getDepId())
          .username(a.getUsername())
          .roles(a.getRoles())
          .build();
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  /**
   * Создает описание аутентификации
   *
   * @param info AuthInfo
   * @return Описание аутентификации
   */
  @Override
  public String toString(AuthInfo info) {
    if (info == null) {
      return null;
    }
    try {
      JsonAuth a = new JsonAuth();
      a.setUserIP(info.getUserIP());
      a.setUserId(info.getUserId());
      a.setOrgId(info.getOrgId());
      a.setUsername(info.getUsername());
      a.setRoles(info.getRoles());
      return OBJECT_MAPPER.writeValueAsString(a);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }
}
