package mp.jprime.dataaccess.templatevalues;

import mp.jprime.dataaccess.enums.FilterValue;
import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * PREV_YEAR - Предыдущий год
 */
@Service
public class PrevYearTemplateValue implements JPTemplateValue {

  @Override
  public String getTemplate() {
    return FilterValue.PREV_YEAR_TEMPLATE;
  }

  @Override
  public Object getValue(Object value, AuthInfo auth) {
    return LocalDate.now().getYear() - 1;
  }
}
