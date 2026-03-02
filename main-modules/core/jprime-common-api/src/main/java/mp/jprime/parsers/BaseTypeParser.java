package mp.jprime.parsers;

/**
 * Базовый класс парсера типов
 */
public abstract class BaseTypeParser<F, T extends Comparable> implements TypeParser<F, T> {
  protected ParserService parserService;

  @Override
  public void setParserService(ParserService parserService) {
    this.parserService = parserService;
  }
}
