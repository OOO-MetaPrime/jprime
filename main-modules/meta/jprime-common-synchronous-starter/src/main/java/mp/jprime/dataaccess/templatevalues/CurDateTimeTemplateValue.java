package mp.jprime.dataaccess.templatevalues;

import mp.jprime.dataaccess.enums.FilterValue;
import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * CUR_DATETIME - Текущее дата+время
 */
@Service
public class CurDateTimeTemplateValue implements JPTemplateValue {

  @Override
  public String getTemplate() {
    return FilterValue.CUR_DATETIME_TEMPLATE;
  }

  @Override
  public Object getValue(Object value, AuthInfo auth) {
    return LocalDateTime.now();
  }
}
