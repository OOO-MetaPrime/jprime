package mp.jprime.dataaccess.params.query.filters.attr;

import mp.jprime.dataaccess.enums.FilterOperation;

/**
 * Строгое равно строк
 */
public class StrictEQ extends AttrValueFilter<String> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public StrictEQ(String attrCode, String value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.STRICT_EQ;
  }

  @Override
  public StrictEQ ofAttr(String attrCode) {
    return new StrictEQ(attrCode, this.getValue());
  }
}
