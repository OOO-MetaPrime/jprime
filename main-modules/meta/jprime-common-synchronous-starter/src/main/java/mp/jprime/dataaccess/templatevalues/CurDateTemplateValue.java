package mp.jprime.dataaccess.templatevalues;

import mp.jprime.dataaccess.enums.FilterValue;
import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * CUR_DATE - Текущий день
 */
@Service
public class CurDateTemplateValue implements JPTemplateValue {

  @Override
  public String getTemplate() {
    return FilterValue.CUR_DATE_TEMPLATE;
  }

  @Override
  public Object getValue(Object value, AuthInfo auth) {
    return LocalDate.now();
  }
}
