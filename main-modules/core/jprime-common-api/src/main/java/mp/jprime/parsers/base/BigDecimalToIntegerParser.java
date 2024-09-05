package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import mp.jprime.parsers.exceptions.JPParseException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * BigDecimal -> Integer
 */
@Service
public final class BigDecimalToIntegerParser implements TypeParser<BigDecimal, Integer> {
  public final static BigDecimal MAX_INTEGER = BigDecimal.valueOf(Integer.MAX_VALUE);
  public final static BigDecimal MIN_INTEGER = BigDecimal.valueOf(Integer.MIN_VALUE);
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Integer parse(BigDecimal value) {
    if (value == null) {
      return null;
    }
    if (value.compareTo(MIN_INTEGER) < 0 || value.compareTo(MAX_INTEGER) > 0) {
      throw new JPParseException("integer.parse", "Значение превышает размерность");
    }
    return value.intValue();
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<BigDecimal> getInputType() {
    return BigDecimal.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<Integer> getOutputType() {
    return Integer.class;
  }
}