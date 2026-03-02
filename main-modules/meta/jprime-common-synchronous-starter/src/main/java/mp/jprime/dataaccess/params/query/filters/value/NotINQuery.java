package mp.jprime.dataaccess.params.query.filters.value;

import mp.jprime.dataaccess.enums.FilterOperation;
import mp.jprime.dataaccess.params.JPSubQuery;


/**
 * Не в указанном подзапросе
 */
public class NotINQuery extends CustomValueFilter<JPSubQuery> {
  /**
   * Конструктор
   *
   * @param customValue Произвольное значение
   * @param value       Условие
   */
  public NotINQuery(Object customValue, JPSubQuery value) {
    super(customValue, value);
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
}
