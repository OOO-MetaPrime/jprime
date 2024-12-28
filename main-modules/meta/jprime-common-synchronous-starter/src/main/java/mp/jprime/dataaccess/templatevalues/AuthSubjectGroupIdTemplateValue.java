package mp.jprime.dataaccess.templatevalues;

import mp.jprime.dataaccess.enums.FilterValue;
import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * AUTH_SUBJECT_GROUP - Предметные группы пользователя
 */
@Service
public class AuthSubjectGroupIdTemplateValue implements JPTemplateValue {

  @Override
  public String getTemplate() {
    return FilterValue.AUTH_SUBJECT_GROUP_TEMPLATE;
  }

  @Override
  public Collection<Integer> getValue(Object value, AuthInfo auth) {
    Collection<Integer> subjectGroups = auth != null ? auth.getSubjectGroups() : null;
    return subjectGroups == null || subjectGroups.isEmpty() ? null : subjectGroups;
  }
}
