package mp.jprime.dataaccess.params.query.filters.attr;

import mp.jprime.dataaccess.enums.FilterOperation;

/**
 * Нечеткий поиск(без парсинга строки)
 */
public class FuzzyQuery extends AttrValueFilter<String> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public FuzzyQuery(String attrCode, String value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.FUZZY_QUERY;
  }

  @Override
  public FuzzyQuery ofAttr(String attrCode) {
    return new FuzzyQuery(attrCode, this.getValue());
  }
}
