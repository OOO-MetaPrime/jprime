package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import mp.jprime.parsers.exceptions.JPParseException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * BigInteger -> Long
 */
@Service
public final class BigIntegerToLongParser extends BaseTypeParser<BigInteger, Long> {
  public final static BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
  public final static BigInteger MIN_LONG =  BigInteger.valueOf(Long.MIN_VALUE);

  @Override
  public Long parse(BigInteger value) {
    if (value == null) {
      return null;
    }
    if (value.compareTo(MIN_LONG) < 0 || value.compareTo(MAX_LONG) > 0) {
      throw new JPParseException("long.parse", "Значение превышает размерность");
    }
    return value.longValue();
  }

  @Override
  public Class<BigInteger> getInputType() {
    return BigInteger.class;
  }

  @Override
  public Class<Long> getOutputType() {
    return Long.class;
  }
}

