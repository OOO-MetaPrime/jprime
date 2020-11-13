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
  /**
   * Признак неизменяемой настройки
   */
  private final boolean immutable;

  private PolicySetBean(String name, String qName, PolicyTarget target,
                        Collection<Policy> policies, boolean immutable) {
    this.name = name;
    this.qName = qName;
    this.target = target;
    this.policies = Collections.unmodifiableCollection(policies != null ? policies : Collections.emptyList());
    this.immutable = immutable;
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

  /**
   * Признак неизменяемой настройки
   *
   * @return Да/Нет
   */
  @Override
  public boolean isImmutable() {
    return immutable;
  }


  public static Builder newBuilder(String name) {
    return new Builder(name);
  }

  public static final class Builder {
    private String name;
    private String qName;
    private PolicyTarget target;
    private Collection<Policy> policies;
    private boolean immutable = true;

    private Builder(String name) {
      this.name = name;
    }

    public PolicySetBean build() {
      return new PolicySetBean(name, qName, target, policies, immutable);
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

    /**
     * Признак неизменяемости
     *
     * @param immutable Признак неизменяемости
     * @return Builder
     */
    public Builder immutable(boolean immutable) {
      this.immutable = immutable;
      return this;
    }
  }
}
