package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.conds.ValueCond;
import mp.jprime.dataaccess.params.query.Filter;

/**
 * Условие по значениям
 */
public abstract class ValueFilter<T> extends Filter implements ValueCond<T> {
  private String attrCode;
  private T attrValue;

  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Значение
   */
  protected ValueFilter(String attrCode, T value) {
    this.attrCode = attrCode;
    this.attrValue = value;
  }

  /**
   * Кодовое имя атрибута
   *
   * @return Кодовое имя атрибута
   */
  public String getAttrCode() {
    return attrCode;
  }

  /**
   * Значение атрибута
   *
   * @return Значение атрибута
   */
  @Override
  public T getValue() {
    return attrValue;
  }
}
