package mp.jprime.security.abac.xmlloader.services;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mp.jprime.dataaccess.JPAction;
import mp.jprime.dataaccess.conds.CollectionCond;
import mp.jprime.dataaccess.conds.InCond;
import mp.jprime.dataaccess.conds.NotInCond;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.parsers.ParserService;
import mp.jprime.security.abac.*;
import mp.jprime.security.abac.beans.*;
import mp.jprime.security.abac.xmlloader.beans.*;
import mp.jprime.security.beans.JPAccessType;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Загрузка настроек ABAC из xml
 */
@Service
public class JPAbacXmlLoader implements JPAbacLoader {
  private static final Logger LOG = LoggerFactory.getLogger(JPAbacXmlLoader.class);
  /**
   * Папка с описанием настроек доступа к xml
   */
  public static final String RESOURCES_FOLDER = "abac/";

  private ParserService parserService;

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
    URL url = null;
    try {
      url = ResourceUtils.getURL("classpath:" + JPAbacXmlLoader.RESOURCES_FOLDER);
    } catch (FileNotFoundException e) {
      LOG.debug(e.getMessage(), e);
    }
    if (url == null) {
      return;
    }
    try {
      Path path = toPath(url);
      if (path == null || !Files.exists(path)) {
        return;
      }
      try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
        for (Path entry : stream) {
          XmlJpAbac xmlJpAbac = new XmlMapper().readValue(Files.newBufferedReader(entry), XmlJpAbac.class);
          if (xmlJpAbac == null || xmlJpAbac.getJpPolicySets() == null) {
            continue;
          }
          XmlJpPolicySets policySets = xmlJpAbac.getJpPolicySets();
          if (policySets.getJpPolicySet() == null || policySets.getJpPolicySet().length == 0) {
            continue;
          }
          for (XmlJpPolicySet policySet : policySets.getJpPolicySet()) {
            XmlJpClasses jpClasses = policySet.getJpClasses();
            XmlJpPolicies jpPolicies = policySet.getJpPolicies();

            PolicySet set = PolicySetBean.newBuilder(policySet.getName())
                .qName(policySet.getqName())
                .target(
                    jpClasses == null || jpClasses.getJpClass() == null ? null : PolicyTargetBean.from(Arrays.asList(jpClasses.getJpClass()))
                )
                .policies(jpPolicies == null ? null : toPolicies(jpPolicies.getJpPolicy()))
                .build();

            sink.next(set);
          }
        }
      }
    } catch (IOException e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  private Collection<Policy> toPolicies(XmlJpPolicy[] jpPolicies) {
    Collection<Policy> policies = new ArrayList<>();
    for (XmlJpPolicy jpPolicy : jpPolicies) {
      XmlJpActions actions = jpPolicy.getActions();
      policies.add(
          PolicyBean.newBuilder(jpPolicy.getName())
              .qName(jpPolicy.getqName())
              .actions(
                  actions == null || actions.getJpAction() == null ? null :
                      Arrays.stream(actions.getJpAction())
                          .map(JPAction::getType)
                          .filter(Objects::nonNull)
                          .collect(Collectors.toList())
              )
              .subjectRules(toSubjectRules(jpPolicy.getSubjectRules()))
              .resourceRules(toResourceRules(jpPolicy.getResourceRules()))
              .environmentRules(toEnvironmentRules(jpPolicy.getEnvironmentRules()))
              .build()
      );
    }
    return policies;
  }

  private Collection<SubjectRule> toSubjectRules(XmlJpSubjectRules jpSubjectRules) {
    if (jpSubjectRules == null || jpSubjectRules.getSubjectRules() == null) {
      return null;
    }
    Collection<SubjectRule> subjectRules = new ArrayList<>();
    for (XmlJpSubjectRule jpSubjectRule : jpSubjectRules.getSubjectRules()) {
      JPAccessType jpAccessType = JPAccessType.getType(jpSubjectRule.getEffect());
      if (jpAccessType == null) {
        continue;
      }
      subjectRules.add(
          SubjectRuleBean.newBuilder(jpSubjectRule.getName(), jpAccessType)
              .qName(jpSubjectRule.getqName())
              .username(toCollectionCond(jpSubjectRule.getUsername()))
              .role(toCollectionCond(jpSubjectRule.getRole()))
              .orgId(toCollectionCond(jpSubjectRule.getOrgId()))
              .depId(toCollectionCond(jpSubjectRule.getDepId()))
              .build()
      );
    }
    return subjectRules;
  }

  private Collection<ResourceRule> toResourceRules(XmlJpResourceRules jpResourceRules) {
    if (jpResourceRules == null || jpResourceRules.getResourceRules() == null) {
      return null;
    }
    Collection<ResourceRule> resourceRules = new ArrayList<>();
    for (XmlJpResourceRule jpResourceRule : jpResourceRules.getResourceRules()) {
      JPAccessType jpAccessType = JPAccessType.getType(jpResourceRule.getEffect());
      if (jpAccessType == null) {
        continue;
      }
      resourceRules.add(
          ResourceRuleBean.newBuilder(
              jpResourceRule.getName(), jpAccessType, jpResourceRule.getAttr(), toCollectionCond(jpResourceRule.getCond())
          )
              .qName(jpResourceRule.getqName())
              .build()
      );
    }
    return resourceRules;
  }

  private Collection<EnvironmentRule> toEnvironmentRules(XmlJpEnvironmentRules jpEnvironmentRules) {
    if (jpEnvironmentRules == null || jpEnvironmentRules.getEnvironmentRules() == null) {
      return null;
    }
    Collection<EnvironmentRule> environmentRules = new ArrayList<>();
    for (XmlJpEnvironmentRule jpEnvironmentRule : jpEnvironmentRules.getEnvironmentRules()) {
      JPAccessType jpAccessType = JPAccessType.getType(jpEnvironmentRule.getEffect());
      if (jpAccessType == null) {
        continue;
      }
      XmlJpTime time = jpEnvironmentRule.getTime();
      environmentRules.add(
          EnvironmentRuleBean.newBuilder(jpEnvironmentRule.getName(), jpAccessType)
              .qName(jpEnvironmentRule.getqName())
              .daysOfWeek(
                  time == null || time.getDaysOfWeek() == null ? null :
                      Stream.of(time.getDaysOfWeek().split(","))
                          .filter(NumberUtils::isCreatable)
                          .map(NumberUtils::createInteger)
                          .filter(i -> i >= 1 && i <= 7)
                          .map(DayOfWeek::of)
                          .collect(Collectors.toList())
              )
              .fromTime(time == null ? null : parserService.parseTo(LocalTime.class, time.getFromTime()))
              .toTime(time == null ? null : parserService.parseTo(LocalTime.class, time.getToTime()))
              .fromDateTime(time == null ? null : parserService.parseTo(LocalDateTime.class, time.getFromDateTime()))
              .toDateTime(time == null ? null : parserService.parseTo(LocalDateTime.class, time.getToDateTime()))
              .ip(toCollectionCond(jpEnvironmentRule.getIp()))
              .build()
      );
    }
    return environmentRules;
  }

  private CollectionCond<String> toCollectionCond(XmlJpCollectionCond cond) {
    if (cond == null) {
      return null;
    }
    XmlJpValues in = cond.getIn();
    XmlJpValues notIn = cond.getNotIn();
    if ((in == null || in.getValue() == null || in.getValue().length == 0) &&
        (notIn == null || notIn.getValue() == null || notIn.getValue().length == 0)) {
      return null;
    }
    if (in != null && in.getValue() != null && in.getValue().length > 0) {
      return InCond.from(Arrays.asList(in.getValue()));
    } else {
      return NotInCond.from(Arrays.asList(notIn.getValue()));
    }
  }
}
