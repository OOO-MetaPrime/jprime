package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * Boolean -> Integer
 */
@Service
public final class BooleanToIntegerParser implements TypeParser<Boolean, Integer> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Integer parse(Boolean value) {
    return value != null && value ? 1 : 0;
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<Boolean> getInputType() {
    return Boolean.class;
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