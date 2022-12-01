package mp.jprime.parsers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Класс для статического вызова вида
 * <p>
 * return ValueParser.parseTo(LocalDate.class, "2022-20-02");
 */
@Service
public final class ValueParser {
  private static ParserService PARSER_SERVICE;

  @Autowired
  public ValueParser(ParserService parserService) {
    this.PARSER_SERVICE = parserService;
  }

  public static <T> T parseTo(Class<T> to, Object value) {
    return PARSER_SERVICE.parseTo(to, value);
  }
}
