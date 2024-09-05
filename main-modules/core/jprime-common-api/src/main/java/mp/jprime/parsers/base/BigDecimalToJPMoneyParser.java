package mp.jprime.parsers.base;

import mp.jprime.lang.JPMoney;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * BigDecimal -> JPMoney
 */
@Service
public final class BigDecimalToJPMoneyParser implements TypeParser<BigDecimal, JPMoney> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public JPMoney parse(BigDecimal value) {
    return value != null ? JPMoney.ofRub(value) : null;
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<BigDecimal> getInputType() {
    return BigDecimal.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<JPMoney> getOutputType() {
    return JPMoney.class;
  }
}