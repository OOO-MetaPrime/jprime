package mp.jprime.security.abac.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Правило - атрибуты ресурса
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonAbacResourceRule {
  private String name;
  private String qName;
  private String effect;
  private String attrCode;
  private JsonAbacCond cond;

  private JsonAbacResourceRule() {

  }

  private JsonAbacResourceRule(String name, String qName, String effect,
                               String attrCode, JsonAbacCond cond) {
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
   * Кодовое имя атрибута
   *
   * @return Кодовое имя атрибута
   */
  public String getAttrCode() {
    return attrCode;
  }

  /**
   * Возвращает условие на атрибуты
   *
   * @return Условие на атрибут
   */
  public JsonAbacCond getCond() {
    return cond;
  }

  public static Builder newBuilder(String name, String effect, String attrCode, JsonAbacCond cond) {
    return new Builder(name, effect, attrCode, cond);
  }

  public static final class Builder {
    private String name;
    private String qName;
    private String effect;
    private String attrCode;
    private JsonAbacCond cond;

    private Builder(String name, String effect, String attrCode, JsonAbacCond cond) {
      this.name = name;
      this.effect = effect;
      this.attrCode = attrCode;
      this.cond = cond;
    }

    public JsonAbacResourceRule build() {
      return new JsonAbacResourceRule(name, qName, effect, attrCode, cond);
    }

    public Builder qName(String qName) {
      this.qName = qName;
      return this;
    }
  }
}
