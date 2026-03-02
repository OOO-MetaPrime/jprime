package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Float -> BigDecimal
 */
@Service
public final class FloatToBigDecimalParser extends BaseTypeParser<Float, BigDecimal> {
  @Override
  public BigDecimal parse(Float value) {
    return value != null ? new BigDecimal(parserService.parseTo(String.class, value)) : null;
  }

  @Override
  public Class<Float> getInputType() {
    return Float.class;
  }

  @Override
  public Class<BigDecimal> getOutputType() {
    return BigDecimal.class;
  }
}