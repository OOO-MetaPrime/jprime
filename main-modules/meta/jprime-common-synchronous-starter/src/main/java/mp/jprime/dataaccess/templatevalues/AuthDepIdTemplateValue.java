package mp.jprime.dataaccess.templatevalues;

import mp.jprime.dataaccess.enums.FilterValue;
import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

/**
 * AUTH_DEPID - Подразделение пользователя
 */
@Service
public class AuthDepIdTemplateValue implements JPTemplateValue {

  @Override
  public String getTemplate() {
    return FilterValue.AUTH_DEPID_TEMPLATE;
  }

  @Override
  public Object getValue(Object value, AuthInfo auth) {
    return auth != null ? auth.getDepId() : null;
  }
}
