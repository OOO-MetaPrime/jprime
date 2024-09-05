package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * Integer -> Boolean
 */
@Service
public final class IntegerToBooleanParser implements TypeParser<Integer, Boolean> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Boolean parse(Integer value) {
    return value != null && value == 1;
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<Integer> getInputType() {
    return Integer.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<Boolean> getOutputType() {
    return Boolean.class;
  }
}