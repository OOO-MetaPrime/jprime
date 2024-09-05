package mp.jprime.dataaccess.templatevalues;

import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

/**
 * AUTH_SEPDEPID - обособленное подразделение пользователя
 */
@Service
public class AuthSepDepIdTemplateValue implements TemplateValue {
  /**
   * Возвращает шаблон
   *
   * @return Шаблон
   */
  @Override
  public String getTemplate() {
    return "AUTH_SEPDEPID";
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
    return authInfo != null ? authInfo.getSepDepId() : null;
  }
}
