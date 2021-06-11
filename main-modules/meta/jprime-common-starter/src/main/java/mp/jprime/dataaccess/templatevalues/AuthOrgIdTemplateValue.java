package mp.jprime.dataaccess.templatevalues;

import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

/**
 * AUTH_ORGID - Организация пользователя
 */
@Service
public class AuthOrgIdTemplateValue implements TemplateValue {
  /**
   * Возвращает шаблон
   *
   * @return Шаблон
   */
  @Override
  public String getTemplate() {
    return "AUTH_ORGID";
  }

  /**
   * Форматируем шаблонное значение
   *
   * @param value    Значение
   * @param authInfo AuthInfo
   * @return Значение поля
   */
  @Override
  public Object getValue(Object value, AuthInfo authInfo) {
    return authInfo != null ? authInfo.getOrgId() : null;
  }
}
