package mp.jprime.parsers.base;

import mp.jprime.lang.JPMoney;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * JPMoney -> BigDecimal
 */
@Service
public final class JPMoneyToBigDecimalParser extends BaseTypeParser<JPMoney, BigDecimal> {
  @Override
  public BigDecimal parse(JPMoney value) {
    return value != null ? value.getNumberStripped() : null;
  }

  @Override
  public Class<JPMoney> getInputType() {
    return JPMoney.class;
  }

  @Override
  public Class<BigDecimal> getOutputType() {
    return BigDecimal.class;
  }
}