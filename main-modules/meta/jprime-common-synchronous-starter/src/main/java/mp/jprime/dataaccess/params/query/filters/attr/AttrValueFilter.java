package mp.jprime.dataaccess.params.query.filters.attr;

import mp.jprime.dataaccess.conds.ValueCond;
import mp.jprime.dataaccess.params.query.Filter;

/**
 * Условие по значениям
 */
public abstract class AttrValueFilter<T> extends Filter implements ValueCond<T> {
  private final String attrCode;
  private final T attrValue;

  /**
   * Конструктор
   *
   * @param attrCode Кодовое имя атрибута
   * @param value    Значение
   */
  protected AttrValueFilter(String attrCode, T value) {
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

  /**
   * Копия условия на новый атрибут
   *
   * @param attrCode Код атрибута
   */
  public abstract AttrValueFilter<T> ofAttr(String attrCode);
}
