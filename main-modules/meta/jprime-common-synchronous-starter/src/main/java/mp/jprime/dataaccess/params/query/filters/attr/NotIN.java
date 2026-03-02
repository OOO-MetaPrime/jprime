package mp.jprime.dataaccess.params.query.filters.attr;

import mp.jprime.dataaccess.enums.FilterOperation;

import java.util.Collection;

/**
 * Не в указанном списке
 */
public class NotIN extends AttrValueFilter<Collection<? extends Comparable>> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public NotIN(String attrCode, Collection<? extends Comparable> value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.NOT_IN;
  }

  @Override
  public NotIN ofAttr(String attrCode) {
    return new NotIN(attrCode, this.getValue());
  }
}
