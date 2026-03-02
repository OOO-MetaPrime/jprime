package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * BigDecimal -> String
 */
@Service
public final class BigDecimalToStringParser extends BaseTypeParser<BigDecimal, String> {
  @Override
  public String parse(BigDecimal value) {
    return value == null ? null : value.toPlainString();
  }

  @Override
  public Class<BigDecimal> getInputType() {
    return BigDecimal.class;
  }

  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}

