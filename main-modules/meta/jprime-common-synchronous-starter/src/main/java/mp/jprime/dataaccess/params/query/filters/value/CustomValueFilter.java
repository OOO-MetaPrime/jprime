package mp.jprime.dataaccess.params.query.filters.value;

import mp.jprime.dataaccess.conds.ValueCond;
import mp.jprime.dataaccess.params.query.Filter;

/**
 * Условие по значениям
 */
public abstract class CustomValueFilter<T> extends Filter implements ValueCond<T> {
  private final Object customValue;
  private final T value;

  /**
   * Конструктор
   *
   * @param customValue Произвольное значение
   * @param value       Значение
   */
  protected CustomValueFilter(Object customValue, T value) {
    this.customValue = customValue;
    this.value = value;
  }

  /**
   * Произвольное значение
   *
   * @return Произвольное значение
   */
  public Object getCustomValue() {
    return customValue;
  }

  /**
   * Значение атрибута
   *
   * @return Значение атрибута
   */
  @Override
  public T getValue() {
    return value;
  }
}
