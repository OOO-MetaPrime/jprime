package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * Boolean -> Boolean
 */
@Service
public class BooleanToBooleanParser implements TypeParser<Boolean, Boolean> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Boolean parse(Boolean value) {
    return value;
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
  public Class<Boolean> getOutputType() {
    return Boolean.class;
  }
}