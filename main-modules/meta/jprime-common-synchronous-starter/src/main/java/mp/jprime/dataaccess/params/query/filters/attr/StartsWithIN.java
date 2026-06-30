package mp.jprime.dataaccess.params.query.filters.attr;

import mp.jprime.dataaccess.enums.FilterOperation;

import java.util.Collection;

/**
 * Значение начинается с указанного в списке
 */
public class StartsWithIN extends AttrValueFilter<Collection<String>> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public StartsWithIN(String attrCode, Collection<String> value) {
    super(attrCode, value);
  }

  @Override
  public FilterOperation getOper() {
    return FilterOperation.STARTS_WITH_IN;
  }

  @Override
  public StartsWithIN ofAttr(String attrCode) {
    return new StartsWithIN(attrCode, this.getValue());
  }
}
