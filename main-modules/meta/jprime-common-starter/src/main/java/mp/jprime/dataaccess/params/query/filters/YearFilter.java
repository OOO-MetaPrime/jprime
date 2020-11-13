package mp.jprime.dataaccess.params.query.filters;

/**
 * Условие по году
 */
public abstract class YearFilter extends ValueFilter<Integer> {
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

