package mp.jprime.security.controllers;

import mp.jprime.dataaccess.JPAction;
import mp.jprime.dataaccess.JPObjectAccessService;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.conds.CollectionCond;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.JPSecurityPackage;
import mp.jprime.security.abac.*;
import mp.jprime.security.abac.json.beans.*;
import mp.jprime.security.abac.services.JPAbacStorage;
import mp.jprime.security.beans.JPAccessType;
import mp.jprime.security.json.beans.JsonJPClassAccess;
import mp.jprime.security.json.beans.JsonJPObjectAccess;
import mp.jprime.security.json.beans.JsonSecurityAccess;
import mp.jprime.security.json.beans.JsonSecurityPackage;
import mp.jprime.security.jwt.JWTService;
import mp.jprime.security.services.JPSecurityStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("access/v1")
public class RestAccessController {
  /**
   * Хранилище метаинформации
   */
  private JPMetaStorage metaStorage;
  /**
   * Хранилище настроек RBAC
   */
  private JPSecurityStorage securityManager;
  /**
   * Хранилище настроек ABAC
   */
  private JPAbacStorage abacStorage;
  /**
   * Обработчик JWT
   */
  private JWTService jwtService;
  /**
   * Интерфейс проверки доступа к объекту
   */
  private JPObjectAccessService objectAccessService;

  @Autowired
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @Autowired
  private void setSecurityManager(JPSecurityStorage securityManager) {
    this.securityManager = securityManager;
  }

  @Autowired
  public void setAbacStorage(JPAbacStorage abacStorage) {
    this.abacStorage = abacStorage;
  }

  @Autowired
  private void setJwtService(JWTService jwtService) {
    this.jwtService = jwtService;
  }

  @Autowired
  private void setObjectAccessService(JPObjectAccessService objectAccessService) {
    this.objectAccessService = objectAccessService;
  }

  @ResponseBody
  @GetMapping(value = "jpObjects/{pluralCode}/{objectId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  public Mono<JsonJPObjectAccess> getObjectAccess(ServerWebExchange swe,
                                                  @PathVariable("pluralCode") String pluralCode,
                                                  @PathVariable("objectId") String objectId) {
    JPClass jpClass = metaStorage.getJPClassByPluralCode(pluralCode);
    if (jpClass == null || jpClass.isInner() || objectId == null || objectId.isEmpty()) {
      return Mono.empty();
    }
    AuthInfo authInfo = jwtService.getAuthInfo(swe);

    return Mono.just(toJPObjectAccessModel(jpClass, objectId, authInfo));
  }

  @ResponseBody
  @GetMapping(value = "jpClasses/{classCode}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  public Mono<JsonJPClassAccess> getClassAccess(ServerWebExchange swe,
                                                @PathVariable("classCode") String classCode) {
    JPClass jpClass = metaStorage.getJPClassByCode(classCode);
    if (jpClass == null || jpClass.isInner()) {
      return Mono.empty();
    }
    AuthInfo authInfo = jwtService.getAuthInfo(swe);
    return Mono.just(toJPClassAccessModel(jpClass, authInfo));
  }

  @ResponseBody
  @GetMapping(value = "jpClasses/{classCode}/{attrCode}/{attrValue}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  public Mono<JsonJPClassAccess> getLinkedClassAccess(ServerWebExchange swe,
                                                      @PathVariable("classCode") String classCode,
                                                      @PathVariable("attrCode") String attrCode,
                                                      @PathVariable("attrValue") String attrValue) {
    JPClass jpClass = metaStorage.getJPClassByCode(classCode);
    if (jpClass == null || jpClass.isInner()) {
      return Mono.empty();
    }

    AuthInfo authInfo = jwtService.getAuthInfo(swe);
    return Mono.just(toJPClassAccessModel(jpClass, attrCode, attrValue, authInfo));
  }

  @ResponseBody
  @GetMapping(value = "jpClasses",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  public Flux<JsonJPClassAccess> getClassAccessList(ServerWebExchange swe) {
    Collection<JPClass> classes = metaStorage.getJPClasses();
    if (classes == null) {
      return Flux.empty();
    }
    AuthInfo authInfo = jwtService.getAuthInfo(swe);
    return Flux.fromIterable(classes)
        .filter(x -> !x.isInner())
        .map(x -> toJPClassAccessModel(x, authInfo));
  }

  @ResponseBody
  @GetMapping(value = "jpPackages",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  public Flux<JsonSecurityPackage> getJPPackages(ServerWebExchange swe) {
    Collection<JPSecurityPackage> packages = securityManager.getPackages();
    if (packages == null) {
      return Flux.empty();
    }
    AuthInfo auth = jwtService.getAuthInfo(swe);
    return Flux.fromIterable(packages)
        .map(x -> toSecurityPackage(x, auth));
  }

  @ResponseBody
  @GetMapping(value = "jpPackages/{code}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.Role).AUTH_ACCESS)")
  public Mono<JsonSecurityPackage> getJPPackages(ServerWebExchange swe,
                                                 @PathVariable("code") String code) {
    Collection<JPSecurityPackage> packages = securityManager.getPackages();
    if (packages == null) {
      return Mono.empty();
    }
    AuthInfo auth = jwtService.getAuthInfo(swe);
    return Flux.fromIterable(packages)
        .filter(x -> x.getCode().equals(code))
        .map(x -> toSecurityPackage(x, auth))
        .singleOrEmpty();
  }

  @ResponseBody
  @GetMapping(value = "policySets",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.security.Role).AUTH_ADMIN)")
  public Flux<JsonAbacPolicySet> getJPPolicySets() {
    Collection<PolicySet> sets = abacStorage.getSettings();
    if (sets == null) {
      return Flux.empty();
    }
    return Flux.fromIterable(sets)
        .map(this::toJsonPolicySet);
  }

  @ResponseBody
  @GetMapping(value = "policySets/{code}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.security.Role).AUTH_ADMIN)")
  public Mono<JsonAbacPolicySet> getJPPolicySets(@PathVariable("code") String code) {
    Collection<PolicySet> sets = abacStorage.getSettings();
    if (sets == null) {
      return Mono.empty();
    }
    return Flux.fromIterable(sets)
        .filter(x -> x.getCode().equals(code))
        .map(this::toJsonPolicySet)
        .singleOrEmpty();
  }

  @ResponseBody
  @PostMapping(value = "/policySets/search", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAuthority(T(mp.jprime.security.security.Role).AUTH_ADMIN)")
  public Flux<JsonAbacPolicySet> getJPPolicySets(@RequestBody JsonAbacQuery query) {
    Collection<PolicySet> sets = abacStorage.getSettings();
    if (sets == null) {
      return Flux.empty();
    }
    JPAbacQuery jpQuery = query.toAbacQuery();
    return Flux.fromIterable(sets)
        .map(this::toJsonPolicySet)
        .flatMap(x -> {
          JsonAbacPolicySet set = x.filter(jpQuery);
          return set == null ? Mono.empty() : Mono.just(set);
        });
  }

  private JsonJPObjectAccess toJPObjectAccessModel(JPClass jpClass, String objectId, AuthInfo auth) {
    JsonJPObjectAccess.Builder builder = JsonJPObjectAccess.newBuilder()
        .objectClassCode(jpClass.getCode())
        .objectId(objectId)
        .read(
            objectAccessService.checkRead(JPId.get(jpClass.getCode(), objectId), auth)
        )
        .create(
            objectAccessService.checkCreate(jpClass.getCode(), auth)
        )
        .update(
            objectAccessService.checkUpdate(JPId.get(jpClass.getCode(), objectId), auth)
        )
        .delete(
            objectAccessService.checkDelete(JPId.get(jpClass.getCode(), objectId), auth)
        );
    // Доступ к атрибутам
    jpClass.getAttrs().stream()
        .filter(attr -> securityManager.checkRead(attr.getJpPackage(), auth.getRoles()))
        .forEach(
            attr -> builder.attrEdit(attr.getCode(), securityManager.checkUpdate(attr.getJpPackage(), auth.getRoles()))
        );
    return builder.build();
  }

  private JsonAbacPolicySet toJsonPolicySet(PolicySet policySet) {
    if (policySet == null) {
      return null;
    }

    return JsonAbacPolicySet.newBuilder(policySet.getName())
        .code(policySet.getCode())
        .qName(policySet.getQName())
        .policies(toJsonPolicy(policySet.getPolicies()))
        .target(JsonAbacPolicyTarget.from(policySet.getTarget().getJpClassCodes()))
        .build();
  }

  private Collection<JsonAbacPolicy> toJsonPolicy(Collection<Policy> policies) {
    if (policies == null || policies.isEmpty()) {
      return Collections.emptyList();
    }

    return policies.stream().map(policy ->
        JsonAbacPolicy.newBuilder(policy.getName())
            .actions(toJsonActions(policy.getActions()))
            .environmentRules(toJsonEnvironmentRules(policy.getEnvironmentRules()))
            .subjectRules(toJsonSubjectRules(policy.getSubjectRules()))
            .resourceRules(toJsonResourceRules(policy.getResourceRules()))
            .build()
    ).collect(Collectors.toList());
  }

  private Collection<String> toJsonActions(Collection<JPAction> actions) {
    if (actions == null || actions.isEmpty()) {
      return Collections.emptyList();
    }
    return actions.stream().map(JPAction::getCode).collect(Collectors.toList());
  }

  private Collection<JsonAbacEnvironmentRule> toJsonEnvironmentRules(Collection<EnvironmentRule> environmentRules) {
    if (environmentRules == null || environmentRules.isEmpty()) {
      return Collections.emptyList();
    }

    return environmentRules.stream().map(environment ->
        JsonAbacEnvironmentRule.newBuilder(environment.getName(), environment.getEffect().getCode())
            .daysOfWeek(toJsonDaysOfWeek(environment.getDaysOfWeek()))
            .fromDateTime(environment.getFromDateTime())
            .fromTime(environment.getFromTime())
            .ip(toJsonAbacConds(environment.getIpCond()))
            .qName(environment.getQName())
            .toDateTime(environment.getToDateTime())
            .toTime(environment.getToTime())
            .build()
    ).collect(Collectors.toList());
  }

  private Collection<String> toJsonDaysOfWeek(Collection<DayOfWeek> daysOfWeek) {
    if (daysOfWeek == null || daysOfWeek.isEmpty()) {
      return null;
    }
    return daysOfWeek.stream().map(DayOfWeek::toString).collect(Collectors.toList());
  }

  private JsonAbacCond toJsonAbacConds(CollectionCond<String> cond) {
    if (cond == null) {
      return null;
    }
    return JsonAbacCond.newBuilder().cond(cond.getOper().getCode()).values(cond.getValue()).build();
  }

  private Collection<JsonAbacSubjectRule> toJsonSubjectRules(Collection<SubjectRule> subjectRules) {
    if (subjectRules == null || subjectRules.isEmpty()) {
      return Collections.emptyList();
    }

    return subjectRules.stream().map(subjectRule ->
        JsonAbacSubjectRule.newBuilder(subjectRule.getName(), subjectRule.getEffect().getCode())
            .qName(subjectRule.getQName())
            .depId(toJsonAbacCond(subjectRule.getDepIdCond()))
            .orgId(toJsonAbacCond(subjectRule.getOrgIdCond()))
            .role(toJsonAbacCond(subjectRule.getRoleCond()))
            .username(toJsonAbacCond(subjectRule.getUsernameCond()))
            .build()
    ).collect(Collectors.toList());
  }

  private JsonAbacCond toJsonAbacCond(CollectionCond<String> object) {
    if (object == null) {
      return null;
    }
    return JsonAbacCond.newBuilder().cond(object.getOper().getCode()).values(object.getValue()).build();
  }

  private Collection<JsonAbacResourceRule> toJsonResourceRules(Collection<ResourceRule> resourceRules) {
    if (resourceRules == null || resourceRules.isEmpty()) {
      return Collections.emptyList();
    }

    return resourceRules.stream().map(resourceRule ->
        JsonAbacResourceRule.newBuilder(
            resourceRule.getName(), resourceRule.getEffect().getCode(), resourceRule.getAttrCode(), toJsonAbacConds(resourceRule.getCond()))
            .qName(resourceRule.getQName())
            .build()
    ).collect(Collectors.toList());
  }

  private JsonJPClassAccess toJPClassAccessModel(JPClass jpClass,  AuthInfo auth) {
    return toJPClassAccessModel(jpClass, null, null, auth);
  }

  private JsonJPClassAccess toJPClassAccessModel(JPClass jpClass,  String refAttrCode, Comparable value,  AuthInfo auth) {
    JsonJPClassAccess.Builder builder = JsonJPClassAccess.newBuilder()
        .classCode(jpClass.getCode())
        .read(
            securityManager.checkRead(jpClass.getJpPackage(), auth.getRoles())
        )
        .create(
            objectAccessService.checkCreate(jpClass.getCode(), refAttrCode, value, auth)
        )
        .update(
            securityManager.checkUpdate(jpClass.getJpPackage(), auth.getRoles())
        )
        .delete(
            securityManager.checkDelete(jpClass.getJpPackage(), auth.getRoles())
        );
    // Доступ к атрибутам
    jpClass.getAttrs().stream()
        .filter(attr -> securityManager.checkRead(attr.getJpPackage(), auth.getRoles()))
        .forEach(
            attr -> builder.attrEdit(attr.getCode(), securityManager.checkUpdate(attr.getJpPackage(), auth.getRoles()))
        );
    return builder.build();
  }


  private JsonSecurityPackage toSecurityPackage(JPSecurityPackage pkg, AuthInfo auth) {
    return JsonSecurityPackage.newBuilder()
        .code(pkg.getCode())
        .name(pkg.getName())
        .description(pkg.getDescription())
        .qName(pkg.getQName())
        .accesses(auth == null || !auth.getRoles().contains(mp.jprime.security.security.Role.AUTH_ADMIN) ? null :
            Stream.concat(
                pkg.getPermitAccess().stream()
                    .map(x -> JsonSecurityAccess.newBuilder()
                        .type(JPAccessType.PERMIT.getCode())
                        .role(x.getRole())
                        .read(x.isRead())
                        .create(x.isCreate())
                        .update(x.isUpdate())
                        .delete(x.isDelete())
                        .build())
                , pkg.getProhibitionAccess().stream()
                    .map(x -> JsonSecurityAccess.newBuilder()
                        .type(JPAccessType.PROHIBITION.getCode())
                        .role(x.getRole())
                        .read(x.isRead())
                        .create(x.isCreate())
                        .update(x.isUpdate())
                        .delete(x.isDelete())
                        .build())
            ).collect(Collectors.toList())
        )
        .build();
  }
}
