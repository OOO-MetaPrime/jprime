package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * BigInteger -> BigInteger
 */
@Service
public final class BigIntegerToBigIntegerParser extends BaseTypeParser<BigInteger, BigInteger> {
  @Override
  public BigInteger parse(BigInteger value) {
    return value;
  }

  @Override
  public Class<BigInteger> getInputType() {
    return BigInteger.class;
  }

  @Override
  public Class<BigInteger> getOutputType() {
    return BigInteger.class;
  }
}