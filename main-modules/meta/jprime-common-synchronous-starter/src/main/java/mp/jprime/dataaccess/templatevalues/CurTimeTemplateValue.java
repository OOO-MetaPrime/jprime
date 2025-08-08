package mp.jprime.dataaccess.templatevalues;

import mp.jprime.dataaccess.enums.FilterValue;
import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

/**
 * CUR_TIME - Текущее время
 */
@Service
public class CurTimeTemplateValue implements JPTemplateValue {

  @Override
  public String getTemplate() {
    return FilterValue.CUR_TIME_TEMPLATE;
  }

  @Override
  public Object getValue(Object value, AuthInfo auth) {
    return LocalTime.now();
  }
}
