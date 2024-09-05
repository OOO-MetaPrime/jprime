package mp.jprime.parsers.base;

import mp.jprime.formats.DateFormat;
import mp.jprime.lang.JPDateRange;
import mp.jprime.lang.JPRange;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * String -> JPDateRange
 */
@Service
public final class StringToJPDateRange implements TypeParser<String, JPDateRange> {

  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  @Override
  public JPDateRange parse(String value) {
    if (value == null || value.isEmpty()) {
      return null;
    }
    if (value.equals(JPRange.EMPTY)) {
      return JPDateRange.emptyRange();
    }

    boolean lowerClose = value.charAt(0) == '[';
    boolean upperClose = value.charAt(value.length() - 1) == ']';
    int delim = value.indexOf(',');

    if (delim == -1) {
      throw new IllegalArgumentException("Cannot find comma character");
    }

    String lowerStr = value.substring(1, delim);
    String upperStr = value.substring(delim + 1, value.length() - 1);

    LocalDate lower = null;
    LocalDate upper = null;

    if (!(lowerStr.length() == 0 || lowerStr.endsWith(JPRange.INFINITY))) {
      lower = LocalDate.parse(lowerStr, DateFormat.LOCAL_DATE_FORMAT);
      if (!lowerClose) {
        lower = lower.plusDays(1);
      }
    }

    if (!(upperStr.length() == 0 || upperStr.endsWith(JPRange.INFINITY))) {
      upper = LocalDate.parse(upperStr, DateFormat.LOCAL_DATE_FORMAT);
      if (!upperClose) {
        upper = upper.minusDays(1);
      }
    }

    return JPDateRange.create(lower, upper);
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  @Override
  public Class<String> getInputType() {
    return String.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  @Override
  public Class<JPDateRange> getOutputType() {
    return JPDateRange.class;
  }
}
