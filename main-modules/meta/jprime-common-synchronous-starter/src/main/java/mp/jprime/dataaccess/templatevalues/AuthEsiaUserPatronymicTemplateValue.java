package mp.jprime.dataaccess.templatevalues;

import mp.jprime.dataaccess.enums.FilterValue;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.AuthParams;
import org.springframework.stereotype.Service;

/**
 * AUTH_ESIA_USERPATRONYMIC - ЕСИА отчество пользователя
 */
@Service
public class AuthEsiaUserPatronymicTemplateValue implements JPTemplateValue {

  @Override
  public String getTemplate() {
    return FilterValue.AUTH_ESIA_USERPATRONYMIC_TEMPLATE;
  }

  @Override
  public Object getValue(Object value, AuthInfo auth) {
    AuthParams.Esia esia = auth != null ? auth.getEsia() : null;
    return esia != null ? esia.getSecondname() : null;
  }
}
