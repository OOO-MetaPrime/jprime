package mp.jprime.parsers.base;

import mp.jprime.lang.JPLongArray;
import mp.jprime.parsers.ParserService;
import mp.jprime.parsers.ParserServiceAware;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Integer -> JPLongArray
 */
@Service
public final class IntegerToJPLongArray implements TypeParser<Integer, JPLongArray>, ParserServiceAware {
  private ParserService service;

  @Override
  public void setParserService(ParserService service) {
    this.service = service;
  }

  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public JPLongArray parse(Integer value) {
    if (value == null) {
      return null;
    }
    return JPLongArray.of(Collections.singletonList(service.parseTo(Long.class, value)));
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
  public Class<JPLongArray> getOutputType() {
    return JPLongArray.class;
  }
}
