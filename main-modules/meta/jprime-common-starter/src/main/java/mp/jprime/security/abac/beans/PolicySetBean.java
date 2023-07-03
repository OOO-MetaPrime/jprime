package mp.jprime.security.abac.beans;

import mp.jprime.security.abac.Policy;
import mp.jprime.security.abac.PolicySet;
import mp.jprime.security.abac.PolicyTarget;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * Группа политик
 */
public class PolicySetBean implements PolicySet {
  private final String code;
  private final String name;
  private final String qName;
  private final PolicyTarget target;
  private final Collection<Policy> policies;
  /**
   * Признак неизменяемой настройки
   */
  private final boolean immutable;

  private PolicySetBean(String code, String name, String qName, PolicyTarget target,
                        Collection<Policy> policies, boolean immutable) {
    this.code = code != null ? code : UUID.randomUUID().toString();
    this.name = name;
    this.qName = qName;
    this.target = target;
    this.policies = policies != null ? Collections.unmodifiableCollection(policies) : Collections.emptyList();
    this.immutable = immutable;
  }

  /**
   * Код политики
   *
   * @return Код политики
   */
  @Override
  public String getCode() {
    return code;
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
    private final String name;
    private String code;
    private String qName;
    private PolicyTarget target;
    private Collection<Policy> policies;
    private boolean immutable = true;

    private Builder(String name) {
      this.name = name;
    }

    public PolicySetBean build() {
      return new PolicySetBean(code, name, qName, target, policies, immutable);
    }

    public Builder code(String code) {
      this.code = code;
      return this;
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
