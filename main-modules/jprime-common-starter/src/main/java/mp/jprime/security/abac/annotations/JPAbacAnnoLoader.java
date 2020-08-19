package mp.jprime.security.abac.annotations;

import mp.jprime.common.annotations.JPCond;
import mp.jprime.dataaccess.JPAction;
import mp.jprime.dataaccess.conds.CollectionCond;
import mp.jprime.dataaccess.conds.InCond;
import mp.jprime.dataaccess.conds.NotInCond;
import mp.jprime.parsers.ParserService;
import mp.jprime.security.JPSecuritySettings;
import mp.jprime.security.abac.*;
import mp.jprime.security.abac.beans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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

  /**
   * Вычитывает метаописание
   *
   * @return Список метаописания
   */
  @Override
  public Flux<PolicySet> load() {
    return Flux.create(x -> {
      loadTo(x);
      x.complete();
    });
  }

  private void loadTo(FluxSink<PolicySet> sink) {
    if (setts == null || setts.isEmpty()) {
      return;
    }
    for (JPSecuritySettings s : setts) {
      JPPolicySets p = s.getClass().getAnnotation(JPPolicySets.class);
      if (p == null) {
        continue;
      }
      JPPolicySet[] policySets = p.value();
      if (policySets.length == 0) {
        continue;
      }
      for (JPPolicySet policySet : policySets) {
        String[] jpClasses = policySet.jpClasses();
        JPPolicy[] jpPolicies = policySet.policies();

        PolicySet set = PolicySetBean.newBuilder(policySet.name())
            .qName(policySet.qName())
            .target(
                jpClasses.length == 0 ? null : PolicyTargetBean.from(Arrays.asList(jpClasses))
            )
            .policies(toPolicies(jpPolicies))
            .build();

        sink.next(set);
      }
    }
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
              .enviromentRules(toEnviromentRules(jpPolicy.enviromentRules()))
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

  private Collection<EnviromentRule> toEnviromentRules(JPEnviromentRule[] jpEnviromentRules) {
    Collection<EnviromentRule> enviromentRules = new ArrayList<>();
    for (JPEnviromentRule jpEnviromentRule : jpEnviromentRules) {
      enviromentRules.add(
          EnviromentRuleBean.newBuilder(jpEnviromentRule.name(), jpEnviromentRule.effect())
              .qName(jpEnviromentRule.qName())
              .daysOfWeek(Arrays.asList(jpEnviromentRule.time().daysOfWeek()))
              .fromTime(parserService.parseTo(LocalTime.class, jpEnviromentRule.time().fromTime()))
              .toTime(parserService.parseTo(LocalTime.class, jpEnviromentRule.time().toTime()))
              .fromDateTime(parserService.parseTo(LocalDateTime.class, jpEnviromentRule.time().fromDateTime()))
              .toDateTime(parserService.parseTo(LocalDateTime.class, jpEnviromentRule.time().toDateTime()))
              .ip(toCollectionCond(jpEnviromentRule.ip()))
              .build()
      );
    }
    return enviromentRules;
  }

  private CollectionCond<String> toCollectionCond(JPCond cond) {
    if (cond == null) {
      return null;
    }
    if (cond.in().length == 0 && cond.notIn().length == 0) {
      return null;
    }
    if (cond.in().length > 0) {
      return InCond.from(Arrays.asList(cond.in()));
    } else {
      return NotInCond.from(Arrays.asList(cond.notIn()));
    }
  }
}
