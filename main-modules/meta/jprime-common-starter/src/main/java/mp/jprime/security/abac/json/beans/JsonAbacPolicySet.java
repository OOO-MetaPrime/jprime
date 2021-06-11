package mp.jprime.security.abac.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import mp.jprime.dataaccess.JPAction;
import mp.jprime.security.abac.JPAbacQuery;
import mp.jprime.security.beans.JPAccessType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Группа политик
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonAbacPolicySet {
  private String code;
  private String name;
  private String qName;
  private JsonAbacPolicyTarget target;
  private Collection<JsonAbacPolicy> policies;

  public JsonAbacPolicySet() {
  }

  private JsonAbacPolicySet(String code, String name, String qName, JsonAbacPolicyTarget target,
                            Collection<JsonAbacPolicy> policies) {
    this.code = code;
    this.name = name;
    this.qName = qName;
    this.target = target;
    this.policies = Collections.unmodifiableCollection(policies != null ? policies : Collections.emptyList());
  }

  /**
   * Код
   *
   * @return Код
   */
  public String getCode() {
    return code;
  }

  /**
   * Название правила
   *
   * @return Название правила
   */
  public String getName() {
    return name;
  }

  /**
   * QName правила
   *
   * @return qName правила
   */
  public String getqName() {
    return qName;
  }

  /**
   * Список классов для применения политики
   *
   * @return Список классов
   */
  public JsonAbacPolicyTarget getTarget() {
    return target;
  }

  /**
   * Политики
   *
   * @return Политики
   */
  public Collection<JsonAbacPolicy> getPolicies() {
    return policies;
  }

  public void setPolicies(Collection<JsonAbacPolicy> policies) {
    this.policies = policies;
  }

  public JsonAbacPolicySet filter(JPAbacQuery jpQuery) {
    if (jpQuery == null) {
      return this;
    }
    Collection<String> jpClasses = jpQuery.getJpClassCodes();
    if (jpClasses != null && !jpClasses.isEmpty() &&
        (this.getTarget() == null || !CollectionUtils.containsAny(this.getTarget().getJpClasses(), jpClasses))) {
      return null;
    }
    String name = jpQuery.getName();

    Collection<String> roles = jpQuery.getRoles();

    boolean useRead = jpQuery.useRead();
    boolean useCreate = jpQuery.useCreate();
    boolean useUpdate = jpQuery.useUpdate();
    boolean useDelete = jpQuery.useDelete();

    boolean usePermit = jpQuery.usePermit();
    boolean useProhibition = jpQuery.useProhibition();

    boolean useEnviromentRules = jpQuery.useEnviromentRules();

    Collection<JsonAbacPolicy> filteredPolicies = new ArrayList<>();
    for (JsonAbacPolicy policy : this.getPolicies()) {
      if (StringUtils.hasText(name) && !policy.getName().contains(name)) {
        continue;
      }

      Collection<String> actions = policy.getActions();
      if (useRead && !actions.contains(JPAction.READ.getCode())) {
        continue;
      }
      if (useCreate && !actions.contains(JPAction.CREATE.getCode())) {
        continue;
      }
      if (useUpdate && !actions.contains(JPAction.UPDATE.getCode())) {
        continue;
      }
      if (useDelete && !actions.contains(JPAction.DELETE.getCode())) {
        continue;
      }
      if (useEnviromentRules && policy.getEnvironmentRules().isEmpty()) {
        continue;
      }
      if (roles != null && !roles.isEmpty() &&
          policy.getSubjectRules().stream()
              .noneMatch(x -> x.getRole() != null && CollectionUtils.containsAny(x.getRole().getValues(), roles))) {
        continue;
      }
      if (usePermit &&
          policy.getSubjectRules().stream().noneMatch(x -> JPAccessType.PERMIT.getCode().equals(x.getEffect())) &&
          policy.getResourceRules().stream().noneMatch(x -> JPAccessType.PERMIT.getCode().equals(x.getEffect())) &&
          policy.getEnvironmentRules().stream().noneMatch(x -> JPAccessType.PERMIT.getCode().equals(x.getEffect()))) {
        continue;
      }
      if (useProhibition &&
          policy.getSubjectRules().stream().noneMatch(x -> JPAccessType.PROHIBITION.getCode().equals(x.getEffect())) &&
          policy.getResourceRules().stream().noneMatch(x -> JPAccessType.PROHIBITION.getCode().equals(x.getEffect())) &&
          policy.getEnvironmentRules().stream().noneMatch(x -> JPAccessType.PROHIBITION.getCode().equals(x.getEffect()))) {
        continue;
      }
      filteredPolicies.add(policy);
    }
    if (filteredPolicies.isEmpty()) {
      return null;
    } else {
      this.setPolicies(filteredPolicies);
    }
    return this;
  }

  public static Builder newBuilder(String name) {
    return new Builder(name);
  }

  public static final class Builder {
    private String code;
    private String name;
    private String qName;
    private JsonAbacPolicyTarget target;
    private Collection<JsonAbacPolicy> policies;

    private Builder(String name) {
      this.name = name;
    }

    public JsonAbacPolicySet build() {
      return new JsonAbacPolicySet(code, name, qName, target, policies);
    }

    public Builder code(String code) {
      this.code = code;
      return this;
    }

    public Builder qName(String qName) {
      this.qName = qName;
      return this;
    }

    public Builder target(JsonAbacPolicyTarget target) {
      this.target = target;
      return this;
    }

    public Builder policies(Collection<JsonAbacPolicy> policies) {
      this.policies = policies;
      return this;
    }
  }
}
