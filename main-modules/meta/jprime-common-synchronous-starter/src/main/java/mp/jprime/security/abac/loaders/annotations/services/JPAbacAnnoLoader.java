package mp.jprime.security.abac.loaders.annotations.services;

import mp.jprime.common.annotations.JPCond;
import mp.jprime.dataaccess.JPAction;
import mp.jprime.dataaccess.conds.*;
import mp.jprime.parsers.ParserService;
import mp.jprime.security.JPSecuritySettings;
import mp.jprime.security.abac.*;
import mp.jprime.security.abac.annotations.*;
import mp.jprime.security.abac.beans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Загрузка ABAC по аннотациям
 */
@Service
public class JPAbacAnnoLoader implements JPAbacLoader {
  private Collection<JPSecuritySettings> setts;
  private ParserService parserService;

  /**
   * Считываем аннотации
   */
  @Autowired(required = false)
  private void setSetts(Collection<JPSecuritySettings> setts) {
    this.setts = setts;
  }

  @Autowired
  private void setParserService(ParserService parserService) {
    this.parserService = parserService;
  }

  @Override
  public Collection<PolicySet> load() {
    if (setts == null || setts.isEmpty()) {
      return Collections.emptyList();
    }
    Collection<PolicySet> result = new ArrayList<>();
    for (JPSecuritySettings s : setts) {
      JPPolicySets p = s.getClass().getAnnotation(JPPolicySets.class);
      if (p == null) {
        continue;
      }
      for (JPPolicySet policySet : p.value()) {
        String[] jpClasses = policySet.jpClasses();
        JPPolicy[] jpPolicies = policySet.policies();

        PolicySet set = PolicySetBean.newBuilder(policySet.code(), policySet.name())
            .qName(policySet.qName())
            .target(
                jpClasses.length == 0 ? null : PolicyTargetBean.from(Arrays.asList(jpClasses))
            )
            .policies(toPolicies(jpPolicies))
            .build();

        result.add(set);
      }
    }
    return result;
  }


  private Collection<Policy> toPolicies(JPPolicy[] jpPolicies) {
    Collection<Policy> policies = new ArrayList<>();
    for (JPPolicy jpPolicy : jpPolicies) {
      JPAction[] actions = jpPolicy.actions();
      policies.add(
          PolicyBean.newBuilder(jpPolicy.name())
              .qName(jpPolicy.qName())
              .actions(actions.length == 0 ? null : Arrays.asList(actions))
              .subjectRules(toSubjectRules(jpPolicy.subjectRules()))
              .resourceRules(toResourceRules(jpPolicy.resourceRules()))
              .environmentRules(toEnvironmentRules(jpPolicy.environmentRules()))
              .build()
      );
    }
    return policies;
  }

  private Collection<SubjectRule> toSubjectRules(JPSubjectRule[] jpSubjectRules) {
    Collection<SubjectRule> subjectRules = new ArrayList<>();
    for (JPSubjectRule jpSubjectRule : jpSubjectRules) {
      subjectRules.add(
          SubjectRuleBean.newBuilder(jpSubjectRule.name(), jpSubjectRule.effect())
              .qName(jpSubjectRule.qName())
              .username(toCollectionCond(jpSubjectRule.username()))
              .role(toCollectionCond(jpSubjectRule.role()))
              .orgId(toCollectionCond(jpSubjectRule.orgId()))
              .depId(toCollectionCond(jpSubjectRule.depId()))
              .build()
      );
    }
    return subjectRules;
  }

  private Collection<ResourceRule> toResourceRules(JPResourceRule[] jpResourceRules) {
    Collection<ResourceRule> resourceRules = new ArrayList<>();
    for (JPResourceRule jpResourceRule : jpResourceRules) {
      resourceRules.add(
          ResourceRuleBean.newBuilder(
                  jpResourceRule.name(), jpResourceRule.effect(), jpResourceRule.attr(), toCollectionCond(jpResourceRule.cond())
              )
              .qName(jpResourceRule.qName())
              .build()
      );
    }
    return resourceRules;
  }

  private Collection<EnvironmentRule> toEnvironmentRules(JPEnvironmentRule[] jpEnvironmentRules) {
    Collection<EnvironmentRule> environmentRules = new ArrayList<>();
    for (JPEnvironmentRule jpEnvironmentRule : jpEnvironmentRules) {
      environmentRules.add(
          EnvironmentRuleBean.newBuilder(jpEnvironmentRule.name(), jpEnvironmentRule.effect())
              .qName(jpEnvironmentRule.qName())
              .daysOfWeek(Arrays.asList(jpEnvironmentRule.time().daysOfWeek()))
              .fromTime(parserService.parseTo(LocalTime.class, jpEnvironmentRule.time().fromTime()))
              .toTime(parserService.parseTo(LocalTime.class, jpEnvironmentRule.time().toTime()))
              .fromDateTime(parserService.parseTo(LocalDateTime.class, jpEnvironmentRule.time().fromDateTime()))
              .toDateTime(parserService.parseTo(LocalDateTime.class, jpEnvironmentRule.time().toDateTime()))
              .ip(toCollectionCond(jpEnvironmentRule.ip()))
              .build()
      );
    }
    return environmentRules;
  }

  private CollectionCond<String> toCollectionCond(JPCond cond) {
    if (cond == null) {
      return null;
    }
    String[] in = cond.in();
    String[] notIn = cond.notIn();
    if (in.length == 0 && notIn.length == 0 &&
        !cond.isNull() && !cond.isNotNull()) {
      return null;
    }
    if (in.length > 0) {
      return InCond.from(Arrays.asList(in));
    } else if (notIn.length > 0) {
      return NotInCond.from(Arrays.asList(notIn));
    } else if (cond.isNull()) {
      return IsNullCond.newInstance();
    } else {
      return IsNotNullCond.newInstance();
    }
  }
}
