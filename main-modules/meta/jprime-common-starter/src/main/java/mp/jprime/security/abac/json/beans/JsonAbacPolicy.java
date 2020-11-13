package mp.jprime.security.abac.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;
import java.util.Collections;

/**
 * Политика
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonAbacPolicy {
  private String name;
  private String qName;
  private Collection<String> actions;
  private Collection<JsonAbacSubjectRule> subjectRules;
  private Collection<JsonAbacResourceRule> resourceRules;
  private Collection<JsonAbacEnvironmentRule> environmentRules;

  public JsonAbacPolicy() {

  }

  private JsonAbacPolicy(String name, String qName,
                         Collection<String> actions, Collection<JsonAbacSubjectRule> subjectRules,
                         Collection<JsonAbacResourceRule> resourceRules, Collection<JsonAbacEnvironmentRule> environmentRules) {
    this.name = name;
    this.qName = qName;
    this.actions = Collections.unmodifiableCollection(actions != null ? actions : Collections.emptyList());
    this.subjectRules = Collections.unmodifiableCollection(subjectRules != null ? subjectRules : Collections.emptyList());
    this.resourceRules = Collections.unmodifiableCollection(resourceRules != null ? resourceRules : Collections.emptyList());
    this.environmentRules = Collections.unmodifiableCollection(environmentRules != null ? environmentRules : Collections.emptyList());
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
   * Действия
   *
   * @return Действия
   */
  public Collection<String> getActions() {
    return actions;
  }

  /**
   * Возвращает правила для пользователя
   *
   * @return правила для пользователя
   */
  public Collection<JsonAbacSubjectRule> getSubjectRules() {
    return subjectRules;
  }

  /**
   * Возвращает правила для объекта
   *
   * @return правила для объекта
   */
  public Collection<JsonAbacResourceRule> getResourceRules() {
    return resourceRules;
  }

  /**
   * Возвращает правила для окружения
   *
   * @return правила для окружения
   */
  public Collection<JsonAbacEnvironmentRule> getEnvironmentRules() {
    return environmentRules;
  }

  public static Builder newBuilder(String name) {
    return new Builder(name);
  }

  public static final class Builder {
    private String name;
    private String qName;
    private Collection<String> actions;
    private Collection<JsonAbacSubjectRule> subjectRules;
    private Collection<JsonAbacResourceRule> resourceRules;
    private Collection<JsonAbacEnvironmentRule> environmentRules;

    private Builder(String name) {
      this.name = name;
    }

    public JsonAbacPolicy build() {
      return new JsonAbacPolicy(name, qName, actions, subjectRules, resourceRules, environmentRules);
    }

    public Builder qName(String qName) {
      this.qName = qName;
      return this;
    }

    public Builder actions(Collection<String> actions) {
      this.actions = actions;
      return this;
    }

    public Builder subjectRules(Collection<JsonAbacSubjectRule> subjectRules) {
      this.subjectRules = subjectRules;
      return this;
    }

    public Builder resourceRules(Collection<JsonAbacResourceRule> resourceRules) {
      this.resourceRules = resourceRules;
      return this;
    }

    public Builder environmentRules(Collection<JsonAbacEnvironmentRule> environmentRules) {
      this.environmentRules = environmentRules;
      return this;
    }
  }
}
