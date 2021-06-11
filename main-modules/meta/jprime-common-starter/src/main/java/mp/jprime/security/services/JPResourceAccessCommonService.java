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
import mp.jprime.security.abac.ResourceRule;
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
  private enum RESULT {
    YES, NO, SKIP
  }

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
    return JPResourceAccessBean.from(Boolean.TRUE, getFilter(checks));
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

  private Filter getFilter(Collection<Policy> policies) {
    if (policies == null || policies.isEmpty()) {
      return null;
    }
    // Признак использования фильтра (отключаем, если нет ограниченмя по атрибуту)
    boolean hasFullPermit = false;

    Map<String, Collection<CollectionCond<String>>> permitConds = new HashMap<>();
    Map<String, Collection<CollectionCond<String>>> prohibitionConds = new HashMap<>();
    for (Policy policy : policies) {
      if (policy.getResourceRules().isEmpty()) {
        hasFullPermit = true;
      } else {
        for (ResourceRule rule : policy.getResourceRules()) {
          if (rule.getEffect() == JPAccessType.PERMIT) {
            permitConds
                .computeIfAbsent(rule.getAttrCode(), v -> new ArrayList<>())
                .add(rule.getCond());
          } else {
            prohibitionConds
                .computeIfAbsent(rule.getAttrCode(), v -> new ArrayList<>())
                .add(rule.getCond());
          }
        }
      }
    }

    if (hasFullPermit && prohibitionConds.isEmpty()) {
      return null;
    }

    Collection<Filter> filters = new ArrayList<>();
    addFilter(JPAccessType.PROHIBITION, prohibitionConds, filters);
    if (!hasFullPermit) {
      addFilter(JPAccessType.PERMIT, permitConds, filters);
    }
    return filters.isEmpty() ? null : Filter.and(filters);
  }

  private void addFilter(JPAccessType accessType,
                         Map<String, Collection<CollectionCond<String>>> condsMap,
                         Collection<Filter> filters) {
    if (condsMap.isEmpty()) {
      return;
    }
    for (Map.Entry<String, Collection<CollectionCond<String>>> entryAttrs : condsMap.entrySet()) {
      String attrCode = entryAttrs.getKey();
      Collection<CollectionCond<String>> conds = entryAttrs.getValue();
      if (attrCode == null || attrCode.isEmpty() || conds == null || conds.isEmpty() || conds.contains(null)) {
        continue;
      }
      Collection<String> inValues = new ArrayList<>();
      Collection<String> notInValues = new ArrayList<>();
      for (CollectionCond<String> cond : conds) {
        Collection<String> v = cond.getValue();
        if (v == null) {
          continue;
        }
        if (cond.getOper() == FilterOperation.IN) {
          inValues.addAll(v);
        } else if (cond.getOper() == FilterOperation.NOTIN) {
          notInValues.addAll(v);
        }
      }
      filters.add(
          JPAccessType.PERMIT == accessType ?
              Filter.and(
                  !inValues.isEmpty() ? Filter.attr(attrCode).in(inValues) : null,
                  !notInValues.isEmpty() ? Filter.attr(attrCode).notIn(notInValues) : null
              )
              :
              Filter.and(
                  !inValues.isEmpty() ? Filter.attr(attrCode).notIn(inValues) : null,
                  !notInValues.isEmpty() ? Filter.attr(attrCode).in(notInValues) : null
              )
      );
    }
  }
}
