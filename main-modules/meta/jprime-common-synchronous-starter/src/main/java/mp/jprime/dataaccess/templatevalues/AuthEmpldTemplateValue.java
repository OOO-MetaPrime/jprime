package mp.jprime.dataaccess.templatevalues;

import mp.jprime.dataaccess.enums.FilterValue;
import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

/**
 * AUTH_EMPLID - Штатная единица пользователя
 */
@Service
public class AuthEmpldTemplateValue implements JPTemplateValue {

  @Override
  public String getTemplate() {
    return FilterValue.AUTH_EMPLID_TEMPLATE;
  }

  @Override
  public Object getValue(Object value, AuthInfo auth) {
    return auth != null ? auth.getEmplId() : null;
  }
}
