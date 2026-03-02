package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * BigInteger -> String
 */
@Service
public final class BigIntegerToStringParser extends BaseTypeParser<BigInteger, String> {
  @Override
  public String parse(BigInteger value) {
    return value == null ? null : value.toString();
  }

  @Override
  public Class<BigInteger> getInputType() {
    return BigInteger.class;
  }

  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}

