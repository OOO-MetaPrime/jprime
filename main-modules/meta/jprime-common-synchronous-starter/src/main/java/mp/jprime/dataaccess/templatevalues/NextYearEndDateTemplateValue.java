package mp.jprime.dataaccess.templatevalues;

import mp.jprime.dataaccess.enums.FilterValue;
import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * NEXT_YEAR_END_DATE - Последнее число следующего года
 */
@Service
public class NextYearEndDateTemplateValue implements JPTemplateValue {

  @Override
  public String getTemplate() {
    return FilterValue.NEXT_YEAR_END_DATE_TEMPLATE;
  }

  @Override
  public Object getValue(Object value, AuthInfo auth) {
    return LocalDate.now().plusYears(1).with(TemporalAdjusters.lastDayOfYear());
  }
}
