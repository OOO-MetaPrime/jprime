package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import mp.jprime.parsers.exceptions.JPParseException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * BigDecimal -> Integer
 */
@Service
public final class BigDecimalToIntegerParser extends BaseTypeParser<BigDecimal, Integer> {
  public final static BigDecimal MAX_INTEGER = BigDecimal.valueOf(Integer.MAX_VALUE);
  public final static BigDecimal MIN_INTEGER = BigDecimal.valueOf(Integer.MIN_VALUE);
  @Override
  public Integer parse(BigDecimal value) {
    if (value == null) {
      return null;
    }
    if (value.compareTo(MIN_INTEGER) < 0 || value.compareTo(MAX_INTEGER) > 0) {
      throw new JPParseException("integer.parse", "Значение превышает размерность");
    }
    return value.intValue();
  }

  @Override
  public Class<BigDecimal> getInputType() {
    return BigDecimal.class;
  }

  @Override
  public Class<Integer> getOutputType() {
    return Integer.class;
  }
}