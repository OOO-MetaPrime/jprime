package mp.jprime.parsers.base;

import mp.jprime.lang.JPMoney;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * JPMoney -> BigDecimal
 */
@Service
public final class JPMoneyToBigDecimalParser implements TypeParser<JPMoney, BigDecimal> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public BigDecimal parse(JPMoney value) {
    return value != null ? value.getNumberStripped() : null;
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<JPMoney> getInputType() {
    return JPMoney.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<BigDecimal> getOutputType() {
    return BigDecimal.class;
  }
}