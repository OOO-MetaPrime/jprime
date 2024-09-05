package mp.jprime.dataaccess.templatevalues;

import mp.jprime.security.AuthInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * AUTH_SUBJECT_GROUP - Предметные группы пользователя
 */
@Service
public class AuthSubjectGroupIdTemplateValue implements TemplateValue {
  public static final String CODE = "AUTH_SUBJECT_GROUP";

  /**
   * Возвращает шаблон
   *
   * @return Шаблон
   */
  @Override
  public String getTemplate() {
    return CODE;
  }

  /**
   * Форматируем шаблонное значение
   *
   * @param value    Значение
   * @param authInfo AuthInfo
   * @return Значение поля
   */
  @Override
  public Collection<Integer> getValue(Object value, AuthInfo authInfo) {
    Collection<Integer> subjectGroups = authInfo != null ? authInfo.getSubjectGroups() : null;
    return subjectGroups == null || subjectGroups.isEmpty() ? null : subjectGroups;
  }
}
