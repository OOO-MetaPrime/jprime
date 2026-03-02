package mp.jprime.parsers.base;

import mp.jprime.lang.JPMoney;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * BigDecimal -> JPMoney
 */
@Service
public final class BigDecimalToJPMoneyParser extends BaseTypeParser<BigDecimal, JPMoney> {
  @Override
  public JPMoney parse(BigDecimal value) {
    return value != null ? JPMoney.ofRub(value) : null;
  }

  @Override
  public Class<BigDecimal> getInputType() {
    return BigDecimal.class;
  }

  @Override
  public Class<JPMoney> getOutputType() {
    return JPMoney.class;
  }
}