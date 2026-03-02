package mp.jprime.dataaccess.params.query.filters.value;

import mp.jprime.dataaccess.enums.FilterOperation;
import mp.jprime.dataaccess.params.JPSubQuery;


/**
 * В указанном подзапросе
 */
public class INQuery extends CustomValueFilter<JPSubQuery> {
  /**
   * Конструктор
   *
   * @param customValue Произвольное значение
   * @param value       Условие
   */
  public INQuery(Object customValue, JPSubQuery value) {
    super(customValue, value);
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