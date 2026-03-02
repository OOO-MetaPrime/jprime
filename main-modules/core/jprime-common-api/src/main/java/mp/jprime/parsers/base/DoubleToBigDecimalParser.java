package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Double -> BigDecimal
 */
@Service
public final class DoubleToBigDecimalParser extends BaseTypeParser<Double, BigDecimal> {
  @Override
  public BigDecimal parse(Double value) {
    return value != null ? new BigDecimal(parserService.parseTo(String.class, value)) : null;
  }

  @Override
  public Class<Double> getInputType() {
    return Double.class;
  }

  @Override
  public Class<BigDecimal> getOutputType() {
    return BigDecimal.class;
  }
}