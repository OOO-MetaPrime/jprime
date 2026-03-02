package mp.jprime.dataaccess.params.query.filters.attr;

import mp.jprime.dataaccess.enums.FilterOperation;

import java.util.Collection;

/**
 * В указанном списке
 */
public class IN extends AttrValueFilter<Collection<? extends Comparable>> {
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

  @Override
  public IN ofAttr(String attrCode) {
    return new IN(attrCode, this.getValue());
  }
}
