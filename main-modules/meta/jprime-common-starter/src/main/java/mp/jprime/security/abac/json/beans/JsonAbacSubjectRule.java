package mp.jprime.security.abac.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Правило - субъект доступа
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonAbacSubjectRule {
  private String name;
  private String qName;
  private String effect;
  private JsonAbacCond username;
  private JsonAbacCond role;
  private JsonAbacCond orgId;
  private JsonAbacCond depId;

  public JsonAbacSubjectRule() {

  }

  private JsonAbacSubjectRule(String name, String qName, String effect,
                              JsonAbacCond username, JsonAbacCond role,
                              JsonAbacCond orgId, JsonAbacCond depId) {
    this.name = name;
    this.qName = qName;
    this.effect = effect;
    this.username = username;
    this.role = role;
    this.orgId = orgId;
    this.depId = depId;
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
   * Возвращает тип доступа (разрешительный/запретительный)
   *
   * @return Разрешение/Запрет
   */
  public String getEffect() {
    return effect;
  }

  /**
   * Возвращает условие на логин
   *
   * @return Условие на логин
   */
  public JsonAbacCond getUsername() {
    return username;
  }

  /**
   * Возвращает условие на роли
   *
   * @return Условие на роли
   */
  public JsonAbacCond getRole() {
    return role;
  }

  /**
   * Возвращает условие на организацию
   *
   * @return Условие на организацию
   */
  public JsonAbacCond getOrgId() {
    return orgId;
  }

  /**
   * Возвращает условие на подразделение
   *
   * @return Условие на подразделение
   */
  public JsonAbacCond getDepId() {
    return depId;
  }

  public static Builder newBuilder(String name, String effect) {
    return new Builder(name, effect);
  }

  public static final class Builder {
    private String name;
    private String qName;
    private String effect;
    private JsonAbacCond username;
    private JsonAbacCond role;
    private JsonAbacCond orgId;
    private JsonAbacCond depId;

    private Builder(String name, String effect) {
      this.name = name;
      this.effect = effect;
    }

    public JsonAbacSubjectRule build() {
      return new JsonAbacSubjectRule(name, qName, effect, username, role, orgId, depId);
    }

    public Builder qName(String qName) {
      this.qName = qName;
      return this;
    }

    public Builder username(JsonAbacCond username) {
      this.username = username;
      return this;
    }

    public Builder role(JsonAbacCond role) {
      this.role = role;
      return this;
    }

    public Builder orgId(JsonAbacCond orgId) {
      this.orgId = orgId;
      return this;
    }

    public Builder depId(JsonAbacCond depId) {
      this.depId = depId;
      return this;
    }
  }
}
