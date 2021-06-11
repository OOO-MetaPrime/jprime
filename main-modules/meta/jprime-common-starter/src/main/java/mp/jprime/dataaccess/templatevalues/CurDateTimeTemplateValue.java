package mp.jprime.dataaccess.templatevalues;

import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * CUR_DATETIME - Текущее дата+время
 */
@Service
public class CurDateTimeTemplateValue implements TemplateValue {
  /**
   * Возвращает шаблон
   *
   * @return Шаблон
   */
  @Override
  public String getTemplate() {
    return "CUR_DATETIME";
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
    return LocalDateTime.now();
  }
}
