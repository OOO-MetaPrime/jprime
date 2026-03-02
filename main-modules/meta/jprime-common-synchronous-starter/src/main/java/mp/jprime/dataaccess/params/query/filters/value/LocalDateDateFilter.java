package mp.jprime.dataaccess.params.query.filters.value;


import java.time.LocalDate;

/**
 * Условие по датам
 */
public abstract class LocalDateDateFilter extends CustomValueFilter<LocalDate> {
  /**
   * Конструктор
   *
   * @param customValue Произвольное значение
   * @param value       Значение
   */
  protected LocalDateDateFilter(Object customValue, LocalDate value) {
    super(customValue, value);
  }
}
