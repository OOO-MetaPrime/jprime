package mp.jprime.dataaccess.templatevalues;

import mp.jprime.dataaccess.enums.FilterValue;
import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * AUTH_OKTMO_TREE - дерево ОКТМО пользователя
 */
@Service
public class AuthOktmoTreeTemplateValue implements JPTemplateValue {

  @Override
  public String getTemplate() {
    return FilterValue.AUTH_OKTMO_TREE_TEMPLATE;
  }

  @Override
  public Collection<String> getValue(Object value, AuthInfo auth) {
    Collection<String> oktmoList = auth != null ? auth.getOktmoList() : null;
    return oktmoList == null || oktmoList.isEmpty() ? null : oktmoList;
  }
}
