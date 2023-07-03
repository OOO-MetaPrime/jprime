package mp.jprime.security.abac.beans;

import mp.jprime.dataaccess.JPAction;
import mp.jprime.security.abac.EnvironmentRule;
import mp.jprime.security.abac.Policy;
import mp.jprime.security.abac.ResourceRule;
import mp.jprime.security.abac.SubjectRule;

import java.util.Collection;
import java.util.Collections;

/**
 * Политика
 */
public class PolicyBean implements Policy {
  private final String name;
  private final String qName;
  private final Collection<JPAction> actions;
  private final Collection<SubjectRule> subjectRules;
  private final Collection<ResourceRule> resourceRules;
  private final Collection<EnvironmentRule> environmentRules;

  private PolicyBean(String name, String qName,
                     Collection<JPAction> actions, Collection<SubjectRule> subjectRules,
                     Collection<ResourceRule> resourceRules, Collection<EnvironmentRule> environmentRules) {
    this.name = name;
    this.qName = qName;
    this.actions = actions != null ? Collections.unmodifiableCollection(actions) : Collections.emptyList();
    this.subjectRules = subjectRules != null ? Collections.unmodifiableCollection(subjectRules) : Collections.emptyList();
    this.resourceRules = resourceRules != null ? Collections.unmodifiableCollection(resourceRules) : Collections.emptyList();
    this.environmentRules = environmentRules != null ? Collections.unmodifiableCollection(environmentRules) : Collections.emptyList();
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
   * Действия
   *
   * @return Действия
   */
  @Override
  public Collection<JPAction> getActions() {
    return actions;
  }

  /**
   * Возвращает правила для пользователя
   *
   * @return правила для пользователя
   */
  @Override
  public Collection<SubjectRule> getSubjectRules() {
    return subjectRules;
  }

  /**
   * Возвращает правила для объекта
   *
   * @return правила для объекта
   */
  @Override
  public Collection<ResourceRule> getResourceRules() {
    return resourceRules;
  }

  /**
   * Возвращает правила для окружения
   *
   * @return правила для окружения
   */
  @Override
  public Collection<EnvironmentRule> getEnvironmentRules() {
    return environmentRules;
  }

  public static Builder newBuilder(String name) {
    return new Builder(name);
  }

  public static final class Builder {
    private String name;
    private String qName;
    private Collection<JPAction> actions;
    private Collection<SubjectRule> subjectRules;
    private Collection<ResourceRule> resourceRules;
    private Collection<EnvironmentRule> environmentRules;

    private Builder(String name) {
      this.name = name;
    }

    public PolicyBean build() {
      return new PolicyBean(name, qName, actions, subjectRules, resourceRules, environmentRules);
    }

    public Builder qName(String qName) {
      this.qName = qName;
      return this;
    }

    public Builder actions(Collection<JPAction> actions) {
      this.actions = actions;
      return this;
    }

    public Builder subjectRules(Collection<SubjectRule> subjectRules) {
      this.subjectRules = subjectRules;
      return this;
    }

    public Builder resourceRules(Collection<ResourceRule> resourceRules) {
      this.resourceRules = resourceRules;
      return this;
    }

    public Builder environmentRules(Collection<EnvironmentRule> environmentRules) {
      this.environmentRules = environmentRules;
      return this;
    }
  }
}
