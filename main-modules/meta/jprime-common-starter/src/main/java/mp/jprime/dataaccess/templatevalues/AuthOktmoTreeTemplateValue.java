package mp.jprime.dataaccess.templatevalues;

import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * AUTH_OKTMO_TREE - дерево ОКТМО пользователя
 */
@Service
public class AuthOktmoTreeTemplateValue implements TemplateValue {
  public static final String CODE = "AUTH_OKTMO_TREE";

  /**
   * Возвращает шаблон
   *
   * @return Шаблон
   */
  @Override
  public String getTemplate() {
    return CODE;
  }

  /**
   * Форматируем шаблонное значение
   *
   * @param value    Значение
   * @param authInfo AuthInfo
   * @return Значение поля
   */
  @Override
  public Collection<String> getValue(Object value, AuthInfo authInfo) {
    Collection<String> oktmoList = authInfo != null ? authInfo.getOktmoList() : null;
    return oktmoList == null || oktmoList.isEmpty() ? null : oktmoList;
  }
}
