package mp.jprime.parsers.base;

import mp.jprime.lang.JPDateRange;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeParseException;

/**
 * JPDateRange -> String
 */
@Service
public class JPDateRangeToString implements TypeParser<JPDateRange, String> {

  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   * <p>
   * Создание {@code LocalDate} диапазона из переданной строки:
   * <pre>{@code
   *     Range<LocalDate> closed = Range.localDateRange("[2014-04-28,2015-04-289]");
   *     Range<LocalDate> quoted = Range.localDateRange("[\"2014-04-28\",\"2015-04-28\"]");
   *     Range<LocalDate> iso = Range.localDateRange("[\"2014-04-28\",\"2015-04-28\"]");
   * }</pre>
   * <p>
   * Доступный формат даты:
   * <ul>
   * <li>yyyy-MM-dd</li>
   * <li>yyyy-MM-dd</li>
   * </ul>
   * @throws DateTimeParseException when one of the bounds are invalid.
   */
  @Override
  public String parse(JPDateRange value) {
    return value.asString();
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  @Override
  public Class<JPDateRange> getInputType() {
    return JPDateRange.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}
