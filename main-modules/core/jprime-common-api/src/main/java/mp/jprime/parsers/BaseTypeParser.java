package mp.jprime.parsers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Базовый класс парсера типов
 */
public abstract class BaseTypeParser<F, T extends Comparable> implements TypeParser<F, T> {
  protected static final Logger LOG = LoggerFactory.getLogger(BaseTypeParser.class);

  protected ParserService parserService;

  @Override
  public void setParserService(ParserService parserService) {
    this.parserService = parserService;
  }
}
