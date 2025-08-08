package mp.jprime.dataaccess.templatevalues;

import mp.jprime.dataaccess.enums.FilterValue;
import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * PREV_MONTH - Предыдущий месяц
 */
@Service
public class PrevMonthTemplateValue implements JPTemplateValue {

  @Override
  public String getTemplate() {
    return FilterValue.PREV_MONTH_TEMPLATE;
  }

  @Override
  public Object getValue(Object value, AuthInfo auth) {
    return LocalDate.now().minusMonths(1).getMonthValue();
  }
}
