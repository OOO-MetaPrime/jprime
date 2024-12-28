package mp.jprime.dataaccess.templatevalues;

import mp.jprime.dataaccess.enums.FilterValue;
import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

/**
 * AUTH_USERID - Идентификатор пользователя
 */
@Service
public class AuthUserIdTemplateValue implements JPTemplateValue {

  @Override
  public String getTemplate() {
    return FilterValue.AUTH_USERID_TEMPLATE;
  }

  @Override
  public Object getValue(Object value, AuthInfo auth) {
    return auth != null ? auth.getUserId() : null;
  }
}
