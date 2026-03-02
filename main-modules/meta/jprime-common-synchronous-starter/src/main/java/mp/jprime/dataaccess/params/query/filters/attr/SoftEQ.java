package mp.jprime.dataaccess.params.query.filters.attr;

import mp.jprime.dataaccess.enums.FilterOperation;

/**
 * Мягкое равно строк (без учета регистра, например)
 */
public class SoftEQ extends AttrValueFilter<String> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public SoftEQ(String attrCode, String value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.SOFT_EQ;
  }

  @Override
  public SoftEQ ofAttr(String attrCode) {
    return new SoftEQ(attrCode, this.getValue());
  }
}
