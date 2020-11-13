package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.enums.AnalyticFunction;

/**
 * Условия по вложенным объектам
 */
public abstract class LinkFilter extends Filter {
  private String attrCode;
  private Filter filter;

  /**
   * Конструктор
   *
   * @param attrCode Кодовое имя атрибута
   * @param filter   Описание условия
   */
  protected LinkFilter(String attrCode, Filter filter) {
    this.attrCode = attrCode;
    this.filter = filter;
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
   * Описание условия
   *
   * @return Описание условия
   */
  public Filter getFilter() {
    return filter;
  }

  /**
   * Условие
   *
   * @return Условие
   */
  abstract public AnalyticFunction getFunction();
}
