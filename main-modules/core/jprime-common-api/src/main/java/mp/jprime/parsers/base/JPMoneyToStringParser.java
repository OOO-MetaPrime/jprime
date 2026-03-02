package mp.jprime.parsers.base;

import mp.jprime.lang.JPMoney;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * JPMoney -> String
 */
@Service
public final class JPMoneyToStringParser extends BaseTypeParser<JPMoney, String> {
  @Override
  public String parse(JPMoney value) {
    BigDecimal amount = value != null ? value.getNumberStripped() : null;
    return amount != null ? amount.toPlainString() : null;
  }

  @Override
  public Class<JPMoney> getInputType() {
    return JPMoney.class;
  }

  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}