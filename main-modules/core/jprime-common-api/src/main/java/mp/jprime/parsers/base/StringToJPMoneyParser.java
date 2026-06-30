package mp.jprime.parsers.base;

import mp.jprime.lang.JPMoney;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * String -> JPMoney
 */
@Service
public final class StringToJPMoneyParser extends BaseTypeParser<String, JPMoney> {
  @Override
  public JPMoney parse(String value) {
    return value != null ? JPMoney.ofRub(parserService.parseTo(BigDecimal.class, value)) : null;
  }

  @Override
  public Class<String> getInputType() {
    return String.class;
  }

  @Override
  public Class<JPMoney> getOutputType() {
    return JPMoney.class;
  }
}