package mp.jprime.dataaccess.templatevalues;

import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

/**
 * AUTH_DEPID - Подразделение пользователя
 */
@Service
public class AuthDepIdTemplateValue implements TemplateValue {
  /**
   * Возвращает шаблон
   *
   * @return Шаблон
   */
  @Override
  public String getTemplate() {
    return "AUTH_DEPID";
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
    return authInfo != null ? authInfo.getDepId() : null;
  }
}
