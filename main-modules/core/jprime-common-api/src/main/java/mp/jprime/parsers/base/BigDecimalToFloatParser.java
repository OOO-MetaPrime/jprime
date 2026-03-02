package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import mp.jprime.parsers.exceptions.JPParseException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * BigDecimal -> Float
 */
@Service
public final class BigDecimalToFloatParser extends BaseTypeParser<BigDecimal, Float> {
  public final static BigDecimal MAX_FLOAT = BigDecimal.valueOf(Float.MAX_VALUE);
  public final static BigDecimal MIN_FLOAT = BigDecimal.valueOf(-1 * Float.MAX_VALUE);

  @Override
  public Float parse(BigDecimal value) {
    if (value == null) {
      return null;
    }
    if (value.compareTo(MIN_FLOAT) < 0 || value.compareTo(MAX_FLOAT) > 0) {
      throw new JPParseException("float.parse", "Значение превышает размерность");
    }
    return value.floatValue();
  }

  @Override
  public Class<BigDecimal> getInputType() {
    return BigDecimal.class;
  }

  @Override
  public Class<Float> getOutputType() {
    return Float.class;
  }
}