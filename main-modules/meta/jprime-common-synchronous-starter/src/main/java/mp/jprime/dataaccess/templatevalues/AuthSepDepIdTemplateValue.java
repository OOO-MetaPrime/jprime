package mp.jprime.dataaccess.templatevalues;

import mp.jprime.dataaccess.enums.FilterValue;
import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

/**
 * AUTH_SEPDEPID - обособленное подразделение пользователя
 */
@Service
public class AuthSepDepIdTemplateValue implements JPTemplateValue {

  @Override
  public String getTemplate() {
    return FilterValue.AUTH_SEPDEPID_TEMPLATE;
  }

  @Override
  public Object getValue(Object value, AuthInfo auth) {
    return auth != null ? auth.getSepDepId() : null;
  }
}
