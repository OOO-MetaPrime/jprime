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
public class JPTemplateValueCommonService implements JPTemplateValueService {
  private final Map<String, JPTemplateValue> qValues = new HashMap<>();

  @Autowired(required = false)
  private void setQValues(Collection<JPTemplateValue> values) {
    if (values == null) {
      return;
    }
    for (JPTemplateValue value : values) {
      qValues.put(value.getPattern(), value);
    }
  }

  @Override
  public Object getValue(Object value, AuthInfo auth) {
    JPTemplateValue valueTemplate = getTemplate(value);
    if (valueTemplate != null) {
      return getValue(valueTemplate, value, auth);
    } else {
      return value;
    }
  }

  @Override
  public Object getValue(JPTemplateValue templateValue, Object value, AuthInfo auth) {
    if (templateValue != null) {
      return templateValue.getValue(value, auth);
    } else {
      return value;
    }
  }

  @Override
  public JPTemplateValue getTemplate(Object value) {
    return value instanceof String x && x.length() > 1 && x.charAt(0) == '{' ? qValues.get(value) : null;
  }
}
