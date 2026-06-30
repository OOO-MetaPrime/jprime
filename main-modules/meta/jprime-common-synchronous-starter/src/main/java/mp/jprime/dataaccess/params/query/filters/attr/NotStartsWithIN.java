package mp.jprime.dataaccess.params.query.filters.attr;

import mp.jprime.dataaccess.enums.FilterOperation;

import java.util.Collection;

/**
 * Значение не начинается с указанного в списке
 */
public class NotStartsWithIN extends AttrValueFilter<Collection<String>> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public NotStartsWithIN(String attrCode, Collection<String> value) {
    super(attrCode, value);
  }

  @Override
  public FilterOperation getOper() {
    return FilterOperation.NOT_STARTS_WITH_IN;
  }

  @Override
  public NotStartsWithIN ofAttr(String attrCode) {
    return new NotStartsWithIN(attrCode, this.getValue());
  }
}
