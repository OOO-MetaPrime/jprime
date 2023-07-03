package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Модель запроса агрегации
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonAggregate {
  /**
   * Алиас выборки
   */
  private String alias;
  /**
   * Кодовое имя атрибута
   */
  private String attr;
  /**
   * Оператор
   */
  private String operator;

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getAttr() {
    return attr;
  }

  public void setAttr(String attr) {
    this.attr = attr;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }
}
