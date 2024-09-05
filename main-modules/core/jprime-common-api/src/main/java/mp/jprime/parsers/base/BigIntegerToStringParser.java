package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * BigInteger -> String
 */
@Service
public final class BigIntegerToStringParser implements TypeParser<BigInteger, String> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public String parse(BigInteger value) {
    return value == null ? null : value.toString();
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<BigInteger> getInputType() {
    return BigInteger.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<String> getOutputType() {
    return String.class;
  }
}

