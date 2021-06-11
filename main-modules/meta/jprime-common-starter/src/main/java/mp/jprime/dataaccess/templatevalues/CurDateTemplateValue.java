package mp.jprime.dataaccess.templatevalues;

import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * CUR_DATE - Текущий день
 */
@Service
public class CurDateTemplateValue implements TemplateValue {
  /**
   * Возвращает шаблон
   *
   * @return Шаблон
   */
  @Override
  public String getTemplate() {
    return "CUR_DATE";
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
    return LocalDate.now();
  }
}
