package mp.jprime.dataaccess.templatevalues;

import mp.jprime.dataaccess.enums.FilterValue;
import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * PREV_DATE - Предыдущий день
 */
@Service
public class PrevDateTemplateValue implements JPTemplateValue {

  @Override
  public String getTemplate() {
    return FilterValue.PREV_DATE_TEMPLATE;
  }

  @Override
  public Object getValue(Object value, AuthInfo auth) {
    return LocalDate.now().minusDays(1);
  }
}
