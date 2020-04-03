package mp.jprime.dataaccess.params.query.filters;

import java.time.LocalDate;

/**
 * Условие по датам
 */
public abstract class LocalDateDateFilter extends ValueFilter<LocalDate> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Значение
   */
  protected LocalDateDateFilter(String attrCode, LocalDate value) {
    super(attrCode, value);
  }
}
