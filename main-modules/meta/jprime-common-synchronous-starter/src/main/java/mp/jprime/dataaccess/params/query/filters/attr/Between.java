package mp.jprime.dataaccess.params.query.filters.attr;

import mp.jprime.dataaccess.params.query.data.Pair;
import mp.jprime.dataaccess.enums.FilterOperation;

/**
 * Между
 */
public class Between extends AttrValueFilter<Pair> {
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

  @Override
  public Between ofAttr(String attrCode) {
    return new Between(attrCode, this.getValue());
  }
}
