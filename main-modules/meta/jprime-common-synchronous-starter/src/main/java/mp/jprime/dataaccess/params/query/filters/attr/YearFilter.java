package mp.jprime.dataaccess.params.query.filters.attr;

/**
 * Условие по году
 */
public abstract class YearFilter extends AttrValueFilter<Integer> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Значение
   */
  protected YearFilter(String attrCode, Integer value) {
    super(attrCode, value);
  }
}

