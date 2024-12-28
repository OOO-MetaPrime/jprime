package mp.jprime.security.abac.beans;

import mp.jprime.dataaccess.conds.CollectionCond;
import mp.jprime.security.abac.ResourceRule;
import mp.jprime.security.beans.JPAccessType;

/**
 * Правило - атрибуты ресурса
 */
public class ResourceRuleBean implements ResourceRule {
  private final String name;
  private final String qName;
  private final JPAccessType effect;
  private final String attrCode;
  private final CollectionCond<String> cond;

  private ResourceRuleBean(String name, String qName, JPAccessType effect,
                           String attrCode, CollectionCond<String> cond) {
    this.name = name;
    this.qName = qName;
    this.effect = effect;
    this.attrCode = attrCode;
    this.cond = cond;
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
   * Кодовое имя атрибута
   *
   * @return Кодовое имя атрибута
   */
  @Override
  public String getAttrCode() {
    return attrCode;
  }

  /**
   * Возвращает условие на атрибуты
   *
   * @return Условие на атрибут
   */
  @Override
  public CollectionCond<String> getCond() {
    return cond;
  }

  public static Builder newBuilder(String name, JPAccessType effect, String attrCode, CollectionCond<String> cond) {
    return new Builder(name, effect, attrCode, cond);
  }

  public static final class Builder {
    private String name;
    private String qName;
    private JPAccessType effect;
    private String attrCode;
    private CollectionCond<String> cond;

    private Builder(String name, JPAccessType effect, String attrCode, CollectionCond<String> cond) {
      this.name = name;
      this.effect = effect;
      this.attrCode = attrCode;
      this.cond = cond;
    }

    public ResourceRuleBean build() {
      return new ResourceRuleBean(name, qName, effect, attrCode, cond);
    }

    public Builder qName(String qName) {
      this.qName = qName;
      return this;
    }
  }
}
