package mp.jprime.parsers.base;

import mp.jprime.lang.JPIntegerArray;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * Integer[] -> JPIntegerArray
 */
@Service
public class IntegerArrayToJPIntegerArray implements TypeParser<Integer[], JPIntegerArray> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public JPIntegerArray parse(Integer[] value) {
    return JPIntegerArray.of(value);
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<Integer[]> getInputType() {
    return Integer[].class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<JPIntegerArray> getOutputType() {
    return JPIntegerArray.class;
  }
}
