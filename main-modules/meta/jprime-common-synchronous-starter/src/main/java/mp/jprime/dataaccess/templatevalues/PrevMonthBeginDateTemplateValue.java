package mp.jprime.dataaccess.templatevalues;

import mp.jprime.dataaccess.enums.FilterValue;
import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * PREV_MONTH_BEGIN_DATE - 1 число предыдущего месяца
 */
@Service
public class PrevMonthBeginDateTemplateValue implements JPTemplateValue {

  @Override
  public String getTemplate() {
    return FilterValue.PREV_MONTH_BEGIN_DATE_TEMPLATE;
  }

  @Override
  public Object getValue(Object value, AuthInfo auth) {
    return LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
  }
}
