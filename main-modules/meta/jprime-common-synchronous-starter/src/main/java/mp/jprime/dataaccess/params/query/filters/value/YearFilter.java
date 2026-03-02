package mp.jprime.dataaccess.params.query.filters.value;

/**
 * Условие по году
 */
public abstract class YearFilter extends CustomValueFilter<Integer> {
  /**
   * Конструктор
   *
   * @param customValue Произвольное значение
   * @param value       Значение
   */
  protected YearFilter(Object customValue, Integer value) {
    super(customValue, value);
  }
}

