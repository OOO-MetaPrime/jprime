package mp.jprime.parsers.base;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * JPId -> BigInteger
 */
@Service
public final class JPIdToBigIntegerParser extends BaseTypeParser<JPId, BigInteger> {
  @Override
  public BigInteger parse(JPId value) {
    return value == null ? null : parserService.parseTo(getOutputType(), value.getId());
  }

  @Override
  public Class<JPId> getInputType() {
    return JPId.class;
  }

  @Override
  public Class<BigInteger> getOutputType() {
    return BigInteger.class;
  }
}
