package mp.jprime.utils;

import mp.jprime.parsers.ParserService;
import mp.jprime.parsers.ParserServiceAware;
import org.springframework.stereotype.Service;

@Service
public final class ParseServiceUtils implements ParserServiceAware {
  private static ParserService SERVICE;

  @Override
  public void setParserService(ParserService parserService) {
    ParseServiceUtils.SERVICE = parserService;
  }

  /**
   * Приводит значение к строке
   *
   * @param value Значение
   * @return Значение
   */
  public static String toString(Object value) {
    return SERVICE.toString(value);
  }
}
