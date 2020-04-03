package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.params.query.enums.FilterOperation;

import java.util.Collection;

/**
 * В указанном списке
 */
public class IN extends ValueFilter<Collection<? extends Comparable>> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public IN(String attrCode, Collection<? extends Comparable> value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.IN;
  }
}
