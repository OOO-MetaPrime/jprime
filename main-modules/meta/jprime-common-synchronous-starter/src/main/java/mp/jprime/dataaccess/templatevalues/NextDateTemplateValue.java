package mp.jprime.dataaccess.templatevalues;

import mp.jprime.dataaccess.enums.FilterValue;
import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * NEXT_DATE - Следующий день
 */
@Service
public class NextDateTemplateValue implements JPTemplateValue {

  @Override
  public String getTemplate() {
    return FilterValue.NEXT_DATE_TEMPLATE;
  }

  @Override
  public Object getValue(Object value, AuthInfo auth) {
    return LocalDate.now().plusDays(1);
  }
}
