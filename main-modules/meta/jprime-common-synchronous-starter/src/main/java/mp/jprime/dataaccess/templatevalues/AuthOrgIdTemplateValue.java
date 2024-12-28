package mp.jprime.dataaccess.templatevalues;

import mp.jprime.dataaccess.enums.FilterValue;
import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

/**
 * AUTH_ORGID - Организация пользователя
 */
@Service
public class AuthOrgIdTemplateValue implements JPTemplateValue {

  @Override
  public String getTemplate() {
    return FilterValue.AUTH_ORGID_TEMPLATE;
  }

  @Override
  public Object getValue(Object value, AuthInfo auth) {
    return auth != null ? auth.getOrgId() : null;
  }
}
