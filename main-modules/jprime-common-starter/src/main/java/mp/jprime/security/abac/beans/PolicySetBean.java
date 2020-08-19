package mp.jprime.security.abac.beans;

import mp.jprime.security.abac.Policy;
import mp.jprime.security.abac.PolicySet;
import mp.jprime.security.abac.PolicyTarget;

import java.util.Collection;
import java.util.Collections;

/**
 * Группа политик
 */
public class PolicySetBean implements PolicySet {
  private final String name;
  private final String qName;
  private final PolicyTarget target;
  private final Collection<Policy> policies;

  private PolicySetBean(String name, String qName, PolicyTarget target, Collection<Policy> policies) {
    this.name = name;
    this.qName = qName;
    this.target = target;
    this.policies = Collections.unmodifiableCollection(policies != null ? policies : Collections.emptyList());
  }

  /**
   * Название правила
   *
   * @return Название правила
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * QName правила
   *
   * @return qName правила
   */
  @Override
  public String getQName() {
    return qName;
  }


  /**
   * Список классов для применения политики
   *
   * @return Список классов
   */
  @Override
  public PolicyTarget getTarget() {
    return target;
  }

  /**
   * Политики
   *
   * @return Политики
   */
  @Override
  public Collection<Policy> getPolicies() {
    return policies;
  }

  public static Builder newBuilder(String name) {
    return new Builder(name);
  }

  public static final class Builder {
    private String name;
    private String qName;
    private PolicyTarget target;
    private Collection<Policy> policies;

    private Builder(String name) {
      this.name = name;
    }

    public PolicySetBean build() {
      return new PolicySetBean(name, qName, target, policies);
    }

    public Builder qName(String qName) {
      this.qName = qName;
      return this;
    }

    public Builder target(PolicyTarget target) {
      this.target = target;
      return this;
    }

    public Builder policies(Collection<Policy> policies) {
      this.policies = policies;
      return this;
    }
  }
}
