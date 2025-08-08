package mp.jprime.dataaccess.templatevalues;

import mp.jprime.dataaccess.enums.FilterValue;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.AuthParams;
import org.springframework.stereotype.Service;

/**
 * AUTH_ESIA_USERID - ЕСИА идентификатор пользователя
 */
@Service
public class AuthEsiaUserIdTemplateValue implements JPTemplateValue {

  @Override
  public String getTemplate() {
    return FilterValue.AUTH_ESIA_USERID_TEMPLATE;
  }

  @Override
  public Object getValue(Object value, AuthInfo auth) {
    AuthParams.Esia esia = auth != null ? auth.getEsia() : null;
    return esia != null ? esia.getOid() : null;
  }
}
