package mp.jprime.parsers;

import org.springframework.stereotype.Service;

/**
 * Класс для статического вызова вида
 * <p>
 * return ValueParser.parseTo(LocalDate.class, "2022-20-02");
 */
@Service
public final class ValueParser implements ParserServiceAware {
  private static ParserService PARSER_SERVICE;

  @Override
  public void setParserService(ParserService parserService) {
    PARSER_SERVICE = parserService;
  }

  /**
   * Приводит значение к строке
   *
   * @param value Значение
   * @return Значение
   */
  public static String toString(Object value) {
    return PARSER_SERVICE.toString(value);
  }

  /**
   * Приводит значение к указанному типу
   *
   * @param to    Выходной тип
   * @param value Значение
   * @return Значение
   */
  public static <T> T parseTo(Class<T> to, Object value) {
    return PARSER_SERVICE.parseTo(to, value);
  }
}
