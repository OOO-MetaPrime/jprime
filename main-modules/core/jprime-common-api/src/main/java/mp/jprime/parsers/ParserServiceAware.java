package mp.jprime.parsers;

import org.springframework.beans.factory.Aware;

/**
 * Заполнение ParserService
 */
public interface ParserServiceAware extends Aware {
  /**
   * Устанавливает  ParserService
   *
   * @param parserService ParserService
   */
  void setParserService(ParserService parserService);
}
