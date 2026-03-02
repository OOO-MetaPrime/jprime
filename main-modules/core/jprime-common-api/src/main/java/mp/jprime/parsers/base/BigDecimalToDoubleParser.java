package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import mp.jprime.parsers.exceptions.JPParseException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * BigDecimal -> Double
 */
@Service
public final class BigDecimalToDoubleParser extends BaseTypeParser<BigDecimal, Double> {
  public final static BigDecimal MAX_DOUBLE = BigDecimal.valueOf(Double.MAX_VALUE);
  public final static BigDecimal MIN_DOUBLE = BigDecimal.valueOf(- 1* Double.MAX_VALUE);

  @Override
  public Double parse(BigDecimal value) {
    if (value == null) {
      return null;
    }
    if (value.compareTo(MIN_DOUBLE) < 0 || value.compareTo(MAX_DOUBLE) > 0) {
      throw new JPParseException("integer.parse", "Значение превышает размерность");
    }
    return value.doubleValue();
  }

  @Override
  public Class<BigDecimal> getInputType() {
    return BigDecimal.class;
  }

  @Override
  public Class<Double> getOutputType() {
    return Double.class;
  }
}