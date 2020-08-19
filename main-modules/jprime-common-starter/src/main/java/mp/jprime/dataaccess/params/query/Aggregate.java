package mp.jprime.dataaccess.params.query;

import mp.jprime.dataaccess.enums.AggregationOperator;

import java.util.Objects;

/**
 * Агрегация атрибута
 */
public class Aggregate {
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
  private AggregationOperator operator;

  /**
   * Конструктор
   *
   * @param alias    Алиас выборки
   * @param attr     Кодовое имя атрибута
   * @param operator Оператор
   */
  public Aggregate(String alias, String attr, AggregationOperator operator) {
    this.alias = alias;
    this.attr = attr;
    this.operator = operator;
  }

  /**
   * Алиас выборки
   *
   * @return Алиас выборки
   */
  public String getAlias() {
    return alias;
  }

  /**
   * Кодовое имя атрибута
   *
   * @return Кодовое имя атрибута
   */
  public String getAttr() {
    return attr;
  }

  /**
   * Оператор
   *
   * @return Оператор
   */
  public AggregationOperator getOperator() {
    return operator;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Aggregate aggregate = (Aggregate) o;
    return Objects.equals(alias, aggregate.alias);
  }

  @Override
  public int hashCode() {
    return Objects.hash(alias);
  }
}
