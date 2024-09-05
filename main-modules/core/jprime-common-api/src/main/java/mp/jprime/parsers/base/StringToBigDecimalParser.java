package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * String -> BigDecimal
 */
@Service
public final class StringToBigDecimalParser implements TypeParser<String, BigDecimal> {
  private static final Logger LOG = LoggerFactory.getLogger(StringToBigDecimalParser.class);

  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   *
   * @return Данные в выходном формате
   */
  public BigDecimal parse(String value) {
    if (StringUtils.isBlank(value)) {
      return null;
    }
    char[] result = new char[value.length()];
    int index = 0;

    for (char c : value.toCharArray()) {
      if (c == ',') {
        result[index++] = '.';
      } else if (!Character.isWhitespace(c) && c != '\u00A0') {
        result[index++] = (c);
      }
    }

    return new BigDecimal(new String(result, 0, index));
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<String> getInputType() {
    return String.class;
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
