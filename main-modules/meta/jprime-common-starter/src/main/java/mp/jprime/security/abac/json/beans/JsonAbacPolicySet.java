package mp.jprime.security.abac.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;
import java.util.Collections;

/**
 * Группа политик
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonAbacPolicySet {
  private String guid;
  private String name;
  private String qName;
  private JsonAbacPolicyTarget target;
  private Collection<JsonAbacPolicy> policies;

  public JsonAbacPolicySet() {
  }

  private JsonAbacPolicySet(String guid, String name, String qName, JsonAbacPolicyTarget target,
                            Collection<JsonAbacPolicy> policies) {
    this.guid = guid;
    this.name = name;
    this.qName = qName;
    this.target = target;
    this.policies = Collections.unmodifiableCollection(policies != null ? policies : Collections.emptyList());
  }

  /**
   * Глобальный идентификатор
   *
   * @return Глобальный идентификатор
   */
  public String getGuid() {
    return guid;
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

  public static Builder newBuilder(String name) {
    return new Builder(name);
  }

  public static final class Builder {
    private String guid;
    private String name;
    private String qName;
    private JsonAbacPolicyTarget target;
    private Collection<JsonAbacPolicy> policies;

    private Builder(String name) {
      this.name = name;
    }

    public JsonAbacPolicySet build() {
      return new JsonAbacPolicySet(guid, name, qName, target, policies);
    }

    public Builder guid(String guid) {
      this.guid = guid;
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
