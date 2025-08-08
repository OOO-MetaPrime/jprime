package mp.jprime.dataaccess.templatevalues;

import mp.jprime.dataaccess.enums.FilterValue;
import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * NEXT_YEAR - Следующий год
 */
@Service
public class NextYearTemplateValue implements JPTemplateValue {

  @Override
  public String getTemplate() {
    return FilterValue.NEXT_YEAR_TEMPLATE;
  }

  @Override
  public Object getValue(Object value, AuthInfo auth) {
    return LocalDate.now().getYear() + 1;
  }
}
