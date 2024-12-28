package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.enums.FilterOperation;
import mp.jprime.dataaccess.params.JPSubQuery;

/**
 * В указанном подзапросе
 */
public class INQuery extends ValueFilter<JPSubQuery> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public INQuery(String attrCode, JPSubQuery value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.IN_QUERY;
  }
}