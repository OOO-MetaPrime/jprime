package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Long -> BigDecimal
 */
@Service
public final class LongToBigDecimalParser extends BaseTypeParser<Long, BigDecimal> {
  @Override
  public BigDecimal parse(Long value) {
    return value == null ? null : BigDecimal.valueOf(value);
  }

  @Override
  public Class<Long> getInputType() {
    return Long.class;
  }

  @Override
  public Class<BigDecimal> getOutputType() {
    return BigDecimal.class;
  }
}
