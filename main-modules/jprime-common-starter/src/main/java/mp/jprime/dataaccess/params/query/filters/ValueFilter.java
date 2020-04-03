package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.params.query.enums.FilterOperation;

/**
 * Условие по значениям
 */
public abstract class ValueFilter<T> extends Filter {
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
  public T getValue() {
    return attrValue;
  }

  /**
   * Операция
   *
   * @return Операция
   */
  abstract public FilterOperation getOper();
}
