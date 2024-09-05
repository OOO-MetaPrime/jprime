package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * Long -> Integer
 */
@Service
public final class LongToIntegerParser implements TypeParser<Long, Integer> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Integer parse(Long value) {
    return value == null ? null : value.intValue();
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<Long> getInputType() {
    return Long.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<Integer> getOutputType() {
    return Integer.class;
  }
}
