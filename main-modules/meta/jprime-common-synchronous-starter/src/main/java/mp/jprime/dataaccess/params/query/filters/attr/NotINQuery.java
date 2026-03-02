package mp.jprime.dataaccess.params.query.filters.attr;

import mp.jprime.dataaccess.enums.FilterOperation;
import mp.jprime.dataaccess.params.JPSubQuery;

/**
 * Не в указанном подзапросе
 */
public class NotINQuery extends AttrValueFilter<JPSubQuery> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public NotINQuery(String attrCode, JPSubQuery value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.NOT_IN_QUERY;
  }

  @Override
  public NotINQuery ofAttr(String attrCode) {
    return new NotINQuery(attrCode, this.getValue());
  }
}
