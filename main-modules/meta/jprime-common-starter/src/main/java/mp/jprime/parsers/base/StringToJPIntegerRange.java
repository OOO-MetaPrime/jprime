package mp.jprime.parsers.base;

import mp.jprime.lang.JPIntegerRange;
import mp.jprime.lang.JPRange;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * String -> JPIntegerRange
 */
@Service
public class StringToJPIntegerRange implements TypeParser<String, JPIntegerRange> {

  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  @Override
  public JPIntegerRange parse(String value) {
    if (value == null || value.isEmpty()) {
      return null;
    }
    if (value.equals(JPRange.EMPTY)) {
      return JPIntegerRange.emptyRange();
    }

    boolean lowerClose = value.charAt(0) == '[';
    boolean upperClose = value.charAt(value.length() - 1) == ']';
    int delim = value.indexOf(',');

    if (delim == -1) {
      throw new IllegalArgumentException("Cannot find comma character");
    }

    String lowerStr = value.substring(1, delim);
    String upperStr = value.substring(delim + 1, value.length() - 1);

    Integer lower = null;
    Integer upper = null;

    if (!(lowerStr.length() == 0 || lowerStr.endsWith(JPRange.INFINITY))) {
      lower = Integer.parseInt(lowerStr);
      if (!lowerClose) {
        lower++;
      }
    }

    if (!(upperStr.length() == 0 || upperStr.endsWith(JPRange.INFINITY))) {
      upper = Integer.parseInt(upperStr);
      if (!upperClose) {
        upper--;
      }
    }

    return JPIntegerRange.create(lower, upper);
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
  public Class<JPIntegerRange> getOutputType() {
    return JPIntegerRange.class;
  }
}
