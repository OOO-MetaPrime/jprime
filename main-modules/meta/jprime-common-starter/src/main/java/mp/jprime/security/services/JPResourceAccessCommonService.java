package mp.jprime.security.services;

import mp.jprime.dataaccess.JPAction;
import mp.jprime.dataaccess.conds.CollectionCond;
import mp.jprime.dataaccess.enums.FilterOperation;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.templatevalues.AuthOktmoTemplateValue;
import mp.jprime.dataaccess.templatevalues.AuthOktmoTreeTemplateValue;
import mp.jprime.dataaccess.templatevalues.AuthSubjectGroupIdTemplateValue;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.abac.EnvironmentRule;
import mp.jprime.security.abac.Policy;
import mp.jprime.security.abac.ResourceRule;
import mp.jprime.security.abac.SubjectRule;
import mp.jprime.security.abac.services.JPAbacStorage;
import mp.jprime.security.beans.JPAccessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Реализация JPResourceAccessService
 */
@Service
public class JPResourceAccessCommonService implements JPResourceAccessService {
  private enum RESULT {
    YES, NO, SKIP
  }

  // Хранилище метаинформации
  private JPMetaStorage metaStorage;
  // Хранилище настроек безопасности
  private JPSecurityStorage securityStorage;
  // Хранилище настроек ABAC
  private JPAbacStorage abacStorage;
  // Шаблон ОКТМО
  private static final String AUTH_OKTMO_PATTERN = "{" + AuthOktmoTemplateValue.CODE + "}";
  // Шаблон дерева ОКТМО
  private static final String AUTH_OKTMO_TREE_PATTERN = "{" + AuthOktmoTreeTemplateValue.CODE + "}";
  // Шаблон предметных групп
  private static final String AUTH_SUBJECT_GROUP_PATTERN = "{" + AuthSubjectGroupIdTemplateValue.CODE + "}";

  @Autowired
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @Autowired
  private void setSecurityStorage(JPSecurityStorage securityStorage) {
    this.securityStorage = securityStorage;
  }

  @Autowired
  private void setAbacStorage(JPAbacStorage abacStorage) {
    this.abacStorage = abacStorage;
  }

  /**
   * Проверка доступа на чтение
   *
   * @param classCode Код метаописания
   * @param auth      AuthInfo
   * @return Да/Нет
   */
  @Override
  public JPResourceAccess checkRead(String classCode, AuthInfo auth) {
    JPClass jpClass = metaStorage.getJPClassByCode(classCode);
    if (jpClass == null) {
      return JPResourceAccessBean.from(Boolean.FALSE);
    }
    if (auth != null && !securityStorage.checkRead(jpClass.getJpPackage(), auth.getRoles())) {
      return JPResourceAccessBean.from(Boolean.FALSE);
    }
    return check(abacStorage.getSettings(classCode, JPAction.READ), auth);
  }

  /**
   * Проверка доступа на удаление
   *
   * @param classCode Код метаописания
   * @param auth      AuthInfo
   * @return Да/Нет
   */
  @Override
  public JPResourceAccess checkDelete(String classCode, AuthInfo auth) {
    JPClass jpClass = metaStorage.getJPClassByCode(classCode);
    if (jpClass == null) {
      return JPResourceAccessBean.from(Boolean.FALSE);
    }
    if (auth != null && !securityStorage.checkDelete(jpClass.getJpPackage(), auth.getRoles())) {
      return JPResourceAccessBean.from(Boolean.FALSE);
    }
    return check(abacStorage.getSettings(classCode, JPAction.DELETE), auth);
  }

  /**
   * Проверка доступа на обновление
   *
   * @param classCode Код метаописания
   * @param auth      AuthInfo
   * @return Да/Нет
   */
  @Override
  public JPResourceAccess checkUpdate(String classCode, AuthInfo auth) {
    JPClass jpClass = metaStorage.getJPClassByCode(classCode);
    if (jpClass == null) {
      return JPResourceAccessBean.from(Boolean.FALSE);
    }
    if (auth != null && !securityStorage.checkUpdate(jpClass.getJpPackage(), auth.getRoles())) {
      return JPResourceAccessBean.from(Boolean.FALSE);
    }
    return check(abacStorage.getSettings(classCode, JPAction.UPDATE), auth);
  }

  /**
   * Проверка доступа на создание
   *
   * @param classCode Код метаописания
   * @param auth      AuthInfo
   * @return Да/Нет
   */
  @Override
  public JPResourceAccess checkCreate(String classCode, AuthInfo auth) {
    JPClass jpClass = metaStorage.getJPClassByCode(classCode);
    if (jpClass == null) {
      return JPResourceAccessBean.from(Boolean.FALSE);
    }
    if (auth != null && !securityStorage.checkCreate(jpClass.getJpPackage(), auth.getRoles())) {
      return JPResourceAccessBean.from(Boolean.FALSE);
    }
    return check(abacStorage.getSettings(classCode, JPAction.CREATE), auth);
  }

  private JPResourceAccess check(Collection<Policy> policies, AuthInfo auth) {
    if (policies == null || policies.isEmpty()) {
      return JPResourceAccessBean.from(Boolean.TRUE);
    }
    Collection<Policy> checks = new ArrayList<>(policies.size());
    for (Policy policy : policies) {
      RESULT res = prePIP(policy, auth);
      if (res == RESULT.NO) {
        return JPResourceAccessBean.from(Boolean.FALSE);
      }
      if (res == RESULT.YES) {
        checks.add(policy);
      }
    }
    if (checks.isEmpty()) {
      return JPResourceAccessBean.from(Boolean.FALSE);
    }
    return JPResourceAccessBean.from(Boolean.TRUE, getFilter(checks, auth));
  }

  private RESULT prePIP(Policy policy, AuthInfo auth) {
    for (SubjectRule subjectRule : policy.getSubjectRules()) {
      RESULT res = check(subjectRule, auth);
      if (res == RESULT.NO) {
        return RESULT.NO;
      } else if (res == RESULT.SKIP) {
        return RESULT.SKIP;
      }
    }
    for (EnvironmentRule environmentRule : policy.getEnvironmentRules()) {
      RESULT res = check(environmentRule, auth);
      if (res == RESULT.NO) {
        return RESULT.NO;
      } else if (res == RESULT.SKIP) {
        return RESULT.SKIP;
      }
    }
    return RESULT.YES;
  }

  private RESULT check(SubjectRule rule, AuthInfo auth) {
    if (rule == null) {
      return RESULT.YES;
    }
    CollectionCond<String> usernameCond = rule.getUsernameCond();
    CollectionCond<String> roleCond = rule.getRoleCond();
    CollectionCond<String> orgIdCondCond = rule.getOrgIdCond();
    CollectionCond<String> depIdCondCond = rule.getDepIdCond();
    boolean check = (usernameCond == null || usernameCond.check(auth.getUsername())) &&
        (roleCond == null || roleCond.check(auth.getRoles())) &&
        (orgIdCondCond == null || orgIdCondCond.check(auth.getOrgId())) &&
        (depIdCondCond == null || depIdCondCond.check(auth.getDepId()));
    if (JPAccessType.PERMIT == rule.getEffect()) {
      return check ? RESULT.YES : RESULT.SKIP;
    } else {
      return check ? RESULT.NO : RESULT.YES;
    }
  }

  private RESULT check(EnvironmentRule rule, AuthInfo auth) {
    if (rule == null) {
      return RESULT.YES;
    }
    LocalDateTime curDate = LocalDateTime.now();
    LocalTime curTime = curDate.toLocalTime();
    DayOfWeek curDayOfWeek = curDate.getDayOfWeek();

    boolean check = (rule.getDaysOfWeek().isEmpty() || rule.getDaysOfWeek().contains(curDayOfWeek)) &&
        (rule.getFromDateTime() == null || !rule.getFromDateTime().isAfter(curDate)) &&
        (rule.getToDateTime() == null || !rule.getToDateTime().isBefore(curDate)) &&
        (rule.getFromTime() == null || !rule.getFromTime().isAfter(curTime)) &&
        (rule.getToTime() == null || !rule.getToTime().isBefore(curTime));
    if (JPAccessType.PERMIT == rule.getEffect()) {
      return check ? RESULT.YES : RESULT.SKIP;
    } else {
      return check ? RESULT.NO : RESULT.YES;
    }
  }

  private Filter getFilter(Collection<Policy> policies, AuthInfo auth) {
    if (policies == null || policies.isEmpty()) {
      return null;
    }
    // Признак использования фильтра (отключаем, если нет ограничения по атрибуту)
    boolean hasFullPermit = false;

    Collection<Filter> permitFilters = new ArrayList<>();
    Collection<Filter> prohibitionFilters = new ArrayList<>();
    for (Policy policy : policies) {
      if (policy.getResourceRules().isEmpty()) {
        hasFullPermit = true;
      } else {
        Collection<Filter> allowFilters = new ArrayList<>();
        Collection<Filter> disallowFilters = new ArrayList<>();

        for (ResourceRule rule : policy.getResourceRules()) {
          Filter filter = getFilter(rule, auth);
          if (JPAccessType.PERMIT == rule.getEffect()) {
            allowFilters.add(filter);
          } else {
            disallowFilters.add(filter);
          }
        }

        if (allowFilters.isEmpty()) {
          prohibitionFilters.add(
              Filter.and(disallowFilters)
          );
        } else {
          allowFilters.addAll(disallowFilters);
          permitFilters.add(
              Filter.and(allowFilters)
          );
        }
      }
    }

    if (hasFullPermit && prohibitionFilters.isEmpty()) {
      return null;
    }

    return Filter.and(
        prohibitionFilters.isEmpty() ? null : Filter.and(prohibitionFilters),
        permitFilters.isEmpty() || hasFullPermit ? null : Filter.or(permitFilters)
    );
  }

  private Filter getFilter(ResourceRule rule, AuthInfo auth) {
    JPAccessType accessType = rule.getEffect();
    String attrCode = rule.getAttrCode();
    CollectionCond<String> cond = rule.getCond();

    Collection<String> inValues = null;
    Collection<String> notInValues = null;
    boolean isNullValue = false;
    boolean isNotNullValue = false;

    if (cond.getOper() == FilterOperation.IN) {
      inValues = cond.getValue();
    } else if (cond.getOper() == FilterOperation.NOT_IN) {
      notInValues = cond.getValue();
    } else if (cond.getOper() == FilterOperation.ISNULL) {
      isNullValue = true;
    } else if (cond.getOper() == FilterOperation.ISNOTNULL) {
      isNotNullValue = true;
    }

    if (JPAccessType.PERMIT == accessType) {
      if (inValues != null && !inValues.isEmpty()) {
        return in(attrCode, inValues, auth);
      }
      if (notInValues != null && !notInValues.isEmpty()) {
        return notIn(attrCode, notInValues, auth);
      }
      if (isNullValue) {
        return Filter.attr(attrCode).isNull();
      }
      if (isNotNullValue) {
        return Filter.attr(attrCode).isNotNull();
      }

    } else if (JPAccessType.PROHIBITION == accessType) {
      if (inValues != null && !inValues.isEmpty()) {
        return notIn(attrCode, inValues, auth);
      }
      if (notInValues != null && !notInValues.isEmpty()) {
        return in(attrCode, notInValues, auth);
      }
      if (isNullValue) {
        return Filter.attr(attrCode).isNotNull();
      }
      if (isNotNullValue) {
        return Filter.attr(attrCode).isNull();
      }
    }
    return null;
  }

  private Filter in(String attrCode, Collection<String> values, AuthInfo auth) {
    boolean isOktmoPattern = values.contains(AUTH_OKTMO_PATTERN);
    boolean isOktmoTreePattern = values.contains(AUTH_OKTMO_TREE_PATTERN);
    boolean isSubjectGroupPattern = values.contains(AUTH_SUBJECT_GROUP_PATTERN);
    if (isOktmoPattern || isOktmoTreePattern) {
      Collection<String> oktmoPrefixList = auth != null ? auth.getOktmoPrefixList() : null;
      if (auth != null) {
        if (isOktmoPattern) {
          oktmoPrefixList = auth.getOktmoPrefixList();
        } else {
          oktmoPrefixList = auth.getOktmoTreeList();
        }
      }
      if (oktmoPrefixList != null && !oktmoPrefixList.isEmpty()) {
        return Filter.or(
            oktmoPrefixList.stream()
                .map(x -> Filter.attr(attrCode).startWith(x))
                .collect(Collectors.toList())
        );
      } else {
        return Filter.attr(attrCode).isNull();
      }
    }
    if (isSubjectGroupPattern) {
      Collection<Integer> subjectGroups = auth != null ? auth.getSubjectGroups() : null;
      if (subjectGroups == null || subjectGroups.isEmpty()) {
        return null;
      }
      return Filter.attr(attrCode).in(subjectGroups);
    }
    return Filter.attr(attrCode).in(values);
  }

  private Filter notIn(String attrCode, Collection<String> values, AuthInfo auth) {
    boolean isOktmoPattern = values.contains(AUTH_OKTMO_PATTERN);
    boolean isOktmoTreePattern = values.contains(AUTH_OKTMO_TREE_PATTERN);
    boolean isSubjectGroupPattern = values.contains(AUTH_SUBJECT_GROUP_PATTERN);
    if (isOktmoPattern || isOktmoTreePattern) {
      Collection<String> oktmoPrefixList = auth != null ? auth.getOktmoPrefixList() : null;
      if (auth != null) {
        if (isOktmoPattern) {
          oktmoPrefixList = auth.getOktmoPrefixList();
        } else {
          oktmoPrefixList = auth.getOktmoTreeList();
        }
      }
      if (oktmoPrefixList != null && !oktmoPrefixList.isEmpty()) {
        return Filter.and(
            oktmoPrefixList.stream()
                .map(x -> Filter.attr(attrCode).notStartWith(x))
                .collect(Collectors.toList())
        );
      } else {
        return Filter.attr(attrCode).isNotNull();
      }
    }
    if (isSubjectGroupPattern) {
      Collection<Integer> subjectGroups = auth != null ? auth.getSubjectGroups() : null;
      if (subjectGroups == null || subjectGroups.isEmpty()) {
        return Filter.attr(attrCode).isNotNull();
      }
      return Filter.attr(attrCode).notIn(subjectGroups);
    }
    return Filter.attr(attrCode).notIn(values);
  }
}