package mp.jprime.parsers.base;

import mp.jprime.lang.JPStringArray;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * String[] -> JPStringArray
 */
@Service
public final class StringArrayToJPStringArray implements TypeParser<String[], JPStringArray> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public JPStringArray parse(String[] value) {
    return JPStringArray.of(value);
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<String[]> getInputType() {
    return String[].class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<JPStringArray> getOutputType() {
    return JPStringArray.class;
  }
}
