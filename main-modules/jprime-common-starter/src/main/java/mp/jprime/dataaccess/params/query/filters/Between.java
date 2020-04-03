package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.params.query.data.Pair;
import mp.jprime.dataaccess.params.query.enums.FilterOperation;

/**
 * Между
 */
public class Between extends ValueFilter<Pair> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public Between(String attrCode, Pair value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.BETWEEN;
  }
}
