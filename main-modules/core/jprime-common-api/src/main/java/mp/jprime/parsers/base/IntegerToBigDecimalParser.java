package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Integer -> BigDecimal
 */
@Service
public final class IntegerToBigDecimalParser extends BaseTypeParser<Integer, BigDecimal> {
  @Override
  public BigDecimal parse(Integer value) {
    return value != null ? new BigDecimal(value.toString()) : null;
  }

  @Override
  public Class<Integer> getInputType() {
    return Integer.class;
  }

  @Override
  public Class<BigDecimal> getOutputType() {
    return BigDecimal.class;
  }
}