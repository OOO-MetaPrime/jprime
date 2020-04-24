package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Модель запроса агрегации
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
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
