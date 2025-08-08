package mp.jprime.dataaccess.templatevalues;

import mp.jprime.dataaccess.enums.FilterValue;
import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * NEXT_MONTH_END_DATE - Последнее число следующего месяца
 */
@Service
public class NextMonthEndDateTemplateValue implements JPTemplateValue {

  @Override
  public String getTemplate() {
    return FilterValue.NEXT_MONTH_END_DATE_TEMPLATE;
  }

  @Override
  public Object getValue(Object value, AuthInfo auth) {
    return LocalDate.now().plusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
  }
}
