package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * String -> Boolean
 */
@Service
public final class StringToBooleanParser implements TypeParser<String, Boolean> {
  /**
   * Строковое значение, соответствующее ДА
   */
  private static final String TRUE = "true";
  /**
   * Строковое значение, соответствующее НЕТ
   */
  private static final String FALSE = "false";

  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Boolean parse(String value) {
    return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("t") ||
        value.equalsIgnoreCase("yes") || value.equals("1") || value.equalsIgnoreCase("on") ||
        value.equalsIgnoreCase("Д") || value.equalsIgnoreCase("Да") ||
        value.equalsIgnoreCase("Есть");
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
  public Class<Boolean> getOutputType() {
    return Boolean.class;
  }
}
