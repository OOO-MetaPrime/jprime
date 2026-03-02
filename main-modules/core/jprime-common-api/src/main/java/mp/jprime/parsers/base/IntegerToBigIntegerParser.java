package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * Integer -> BigInteger
 */
@Service
public final class IntegerToBigIntegerParser extends BaseTypeParser<Integer, BigInteger> {
  @Override
  public BigInteger parse(Integer value) {
    return value != null ? new BigInteger(value.toString()) : null;
  }

  @Override
  public Class<Integer> getInputType() {
    return Integer.class;
  }

  @Override
  public Class<BigInteger> getOutputType() {
    return BigInteger.class;
  }
}