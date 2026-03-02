package mp.jprime.parsers;

/**
 * Парсер типов
 */
public interface TypeParser<F, T extends Comparable> {
  /**
   * Прописывает ParserService
   *
   * @param parserService ParserService
   */
  void setParserService(ParserService parserService);

  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  default T parseObject(Object value) {
    return parse((F) value);
  }

  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  T parse(F value);

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  Class<F> getInputType();

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  Class<T> getOutputType();
}
