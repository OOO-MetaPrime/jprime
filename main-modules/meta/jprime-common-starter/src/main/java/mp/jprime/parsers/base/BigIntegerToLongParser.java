package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * BigInteger -> Long
 */
@Service
public class BigIntegerToLongParser implements TypeParser<BigInteger, Long> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Long parse(BigInteger value) {
    return value == null ? null : value.longValue();
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
  public Class<Long> getOutputType() {
    return Long.class;
  }
}

