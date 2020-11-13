package mp.jprime.security.services;

import mp.jprime.dataaccess.JPAction;
import mp.jprime.dataaccess.conds.CollectionCond;
import mp.jprime.dataaccess.enums.FilterOperation;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.abac.EnvironmentRule;
import mp.jprime.security.abac.Policy;
import mp.jprime.security.abac.SubjectRule;
import mp.jprime.security.abac.services.JPAbacStorage;
import mp.jprime.security.beans.JPAccessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Реализация JPResourceAccessService
 */
@Service
public class JPResourceAccessCommonService implements JPResourceAccessService {
  // Хранилище метаинформации
  private JPMetaStorage metaStorage;
  // Хранилище настроек безопасности
  private JPSecurityStorage securityStorage;
  // Хранилище настроек ABAC
  private JPAbacStorage abacStorage;

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
      if (prePIP(policy, auth)) {
        checks.add(policy);
      }
    }
    if (checks.isEmpty()) {
      return JPResourceAccessBean.from(Boolean.FALSE);
    }
    return JPResourceAccessBean.from(Boolean.TRUE, getFilter(checks));
  }

  private boolean prePIP(Policy policy, AuthInfo auth) {
    for (SubjectRule subjectRule : policy.getSubjectRules()) {
      if (!check(subjectRule, auth)) {
        return false;
      }
    }
    for (EnvironmentRule environmentRule : policy.getEnvironmentRules()) {
      if (!check(environmentRule, auth)) {
        return false;
      }
    }
    return true;
  }

  private boolean check(SubjectRule rule, AuthInfo auth) {
    if (rule == null) {
      return Boolean.TRUE;
    }
    CollectionCond<String> usernameCond = rule.getUsernameCond();
    CollectionCond<String> roleCond = rule.getRoleCond();
    CollectionCond<String> orgIdCondCond = rule.getOrgIdCond();
    CollectionCond<String> depIdCondCond = rule.getDepIdCond();
    boolean check = (usernameCond == null || usernameCond.check(auth.getUsername())) &&
        (roleCond == null || roleCond.check(auth.getRoles())) &&
        (orgIdCondCond == null || orgIdCondCond.check(auth.getOrgId())) &&
        (depIdCondCond == null || depIdCondCond.check(auth.getDepId()));
    return (JPAccessType.PERMIT == rule.getEffect()) == check;
  }

  private boolean check(EnvironmentRule rule, AuthInfo auth) {
    if (rule == null) {
      return Boolean.TRUE;
    }
    LocalDateTime curDate = LocalDateTime.now();
    LocalTime curTime = curDate.toLocalTime();
    DayOfWeek curDayOfWeek = curDate.getDayOfWeek();

    boolean check = (rule.getDaysOfWeek().isEmpty() || rule.getDaysOfWeek().contains(curDayOfWeek)) &&
        (rule.getFromDateTime() == null || !rule.getFromDateTime().isAfter(curDate)) &&
        (rule.getToDateTime() == null || !rule.getToDateTime().isBefore(curDate)) &&
        (rule.getFromTime() == null || !rule.getFromTime().isAfter(curTime)) &&
        (rule.getToTime() == null || !rule.getToTime().isBefore(curTime));
    return (JPAccessType.PERMIT == rule.getEffect()) == check;
  }

  private Filter getFilter(Collection<Policy> policies) {
    Map<JPAccessType, Map<String, Collection<CollectionCond<String>>>> allValues = new HashMap<>();
    policies.stream()
        .map(Policy::getResourceRules)
        .filter(Objects::nonNull)
        .forEach(list ->
            list.forEach(cond -> {
              allValues
                  .computeIfAbsent(cond.getEffect(), map -> new HashMap<>())
                  .computeIfAbsent(cond.getAttrCode(), v -> new ArrayList<>())
                  .add(cond.getCond());
            })
        );

    Collection<Filter> filters = new ArrayList<>();
    for (Map.Entry<JPAccessType, Map<String, Collection<CollectionCond<String>>>> entryType : allValues.entrySet()) {
      for (Map.Entry<String, Collection<CollectionCond<String>>> entryAttrs : entryType.getValue().entrySet()) {
        String attrCode = entryAttrs.getKey();
        Collection<CollectionCond<String>> conds = entryAttrs.getValue();
        if (attrCode == null || attrCode.isEmpty() || conds == null || conds.isEmpty() || conds.contains(null)) {
          continue;
        }
        Collection<String> values = new ArrayList<>();
        for (CollectionCond<String> cond : conds) {
          Collection<String> v = cond.getValue();
          if (v == null) {
            continue;
          }
          if (cond.getOper() == FilterOperation.IN) {
            values.addAll(v);
          } else if (cond.getOper() == FilterOperation.NOTIN) {
            values.removeAll(v);
          }
        }
        filters.add(
            JPAccessType.PERMIT == entryType.getKey() ? Filter.attr(attrCode).in(values) : Filter.attr(attrCode).notIn(values)
        );
      }
    }
    return filters.isEmpty() ? null : Filter.and(filters);
  }
}
