package mp.jprime.security.abac.beans;

import mp.jprime.dataaccess.conds.CollectionCond;
import mp.jprime.security.abac.SubjectRule;
import mp.jprime.security.beans.JPAccessType;

/**
 * Правило - субъект доступа
 */
public class SubjectRuleBean implements SubjectRule {
  private final String name;
  private final String qName;
  private final JPAccessType effect;
  private final CollectionCond<String> username;
  private final CollectionCond<String> role;
  private final CollectionCond<String> orgId;
  private final CollectionCond<String> depId;

  private SubjectRuleBean(String name, String qName, JPAccessType effect,
                          CollectionCond<String> username, CollectionCond<String> role,
                          CollectionCond<String> orgId, CollectionCond<String> depId) {
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
   * Возвращает тип доступа (разрешительный/запретительный)
   *
   * @return Разрешение/Запрет
   */
  @Override
  public JPAccessType getEffect() {
    return effect;
  }

  /**
   * Возвращает условие на логин
   *
   * @return Условие на логин
   */
  @Override
  public CollectionCond<String> getUsernameCond() {
    return username;
  }

  /**
   * Возвращает условие на роли
   *
   * @return Условие на роли
   */
  @Override
  public CollectionCond<String> getRoleCond() {
    return role;
  }

  /**
   * Возвращает условие на организацию
   *
   * @return Условие на организацию
   */
  @Override
  public CollectionCond<String> getOrgIdCond() {
    return orgId;
  }

  /**
   * Возвращает условие на подразделение
   *
   * @return Условие на подразделение
   */
  @Override
  public CollectionCond<String> getDepIdCond() {
    return depId;
  }

  public static Builder newBuilder(String name, JPAccessType effect) {
    return new Builder(name, effect);
  }

  public static final class Builder {
    private String name;
    private String qName;
    private JPAccessType effect;
    private CollectionCond<String> username;
    private CollectionCond<String> role;
    private CollectionCond<String> orgId;
    private CollectionCond<String> depId;

    private Builder(String name, JPAccessType effect) {
      this.name = name;
      this.effect = effect;
    }

    public SubjectRuleBean build() {
      return new SubjectRuleBean(name, qName, effect, username, role, orgId, depId);
    }

    public Builder qName(String qName) {
      this.qName = qName;
      return this;
    }

    public Builder username(CollectionCond<String> username) {
      this.username = username;
      return this;
    }

    public Builder role(CollectionCond<String> role) {
      this.role = role;
      return this;
    }

    public Builder orgId(CollectionCond<String> orgId) {
      this.orgId = orgId;
      return this;
    }

    public Builder depId(CollectionCond<String> depId) {
      this.depId = depId;
      return this;
    }
  }
}
