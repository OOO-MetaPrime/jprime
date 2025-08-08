package mp.jprime.dataaccess.templatevalues;

import mp.jprime.dataaccess.enums.FilterValue;
import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * PREV_YEAR_END_DATE - Последнее число предыдущего года
 */
@Service
public class PrevYearEndDateTemplateValue implements JPTemplateValue {

  @Override
  public String getTemplate() {
    return FilterValue.PREV_YEAR_END_DATE_TEMPLATE;
  }

  @Override
  public Object getValue(Object value, AuthInfo auth) {
    return LocalDate.now().minusYears(1).with(TemporalAdjusters.lastDayOfYear());
  }
}
