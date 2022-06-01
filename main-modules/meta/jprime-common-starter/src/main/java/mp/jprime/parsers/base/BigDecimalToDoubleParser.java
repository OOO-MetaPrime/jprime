package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import mp.jprime.parsers.exceptions.JPParseException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * BigDecimal -> Double
 */
@Service
public class BigDecimalToDoubleParser implements TypeParser<BigDecimal, Double> {
  public final static BigDecimal MAX_DOUBLE = BigDecimal.valueOf(Double.MAX_VALUE);
  public final static BigDecimal MIN_DOUBLE = BigDecimal.valueOf(- 1* Double.MAX_VALUE);

  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Double parse(BigDecimal value) {
    if (value == null) {
      return null;
    }
    if (value.compareTo(MIN_DOUBLE) < 0 || value.compareTo(MAX_DOUBLE) > 0) {
      throw new JPParseException("integer.parse", "Значение превышает размерность");
    }
    return value.doubleValue();
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
  public Class<Double> getOutputType() {
    return Double.class;
  }
}