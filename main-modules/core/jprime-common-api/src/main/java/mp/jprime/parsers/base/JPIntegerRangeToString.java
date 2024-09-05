package mp.jprime.parsers.base;

import mp.jprime.lang.JPIntegerRange;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * JPIntegerRange -> String
 */
@Service
public final class JPIntegerRangeToString implements TypeParser<JPIntegerRange, String> {

  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  @Override
  public String parse(JPIntegerRange value) {
    return value.asString();
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  @Override
  public Class<JPIntegerRange> getInputType() {
    return JPIntegerRange.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}
