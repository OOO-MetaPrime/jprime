package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * Long -> BigDecimal
 */
@Service
public final class LongToBigIntegerParser extends BaseTypeParser<Long, BigInteger> {
  @Override
  public BigInteger parse(Long value) {
    return value == null ? null : BigInteger.valueOf(value);
  }

  @Override
  public Class<Long> getInputType() {
    return Long.class;
  }

  @Override
  public Class<BigInteger> getOutputType() {
    return BigInteger.class;
  }
}
