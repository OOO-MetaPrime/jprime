package mp.jprime.dataaccess.templatevalues;

import mp.jprime.security.AuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Сервис получения шаблонных значения
 */
@Service
public class JPTemplateValueDefaultService implements JPTemplateValueService {
  private final Map<String, TemplateValue> qValues = new HashMap<>();

  @Autowired(required = false)
  private void setQValues(Collection<TemplateValue> values) {
    if (values == null) {
      return;
    }
    for (TemplateValue value : values) {
      qValues.put(value.getPattern(), value);
    }
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
    TemplateValue qValue = value instanceof String x && x.length() > 1 && x.charAt(0) == '{' ? qValues.get(value) : null;
    if (qValue != null) {
      return qValue.getValue(value, authInfo);
    } else {
      return value;
    }
  }
}
