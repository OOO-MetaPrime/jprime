package mp.jprime.parsers.base;

import mp.jprime.lang.JPLongArray;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * Long[] -> JPLongArray
 */
@Service
public class LongArrayToJPLongArray implements TypeParser<Long[], JPLongArray> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public JPLongArray parse(Long[] value) {
    return JPLongArray.of(value);
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<Long[]> getInputType() {
    return Long[].class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<JPLongArray> getOutputType() {
    return JPLongArray.class;
  }
}
