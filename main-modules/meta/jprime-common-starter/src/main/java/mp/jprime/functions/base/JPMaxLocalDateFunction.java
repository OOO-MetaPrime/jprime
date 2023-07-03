package mp.jprime.functions.base;

import mp.jprime.functions.JPFunction;
import mp.jprime.parsers.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

/**
 * Найти максимальную дату
 * <p>
 * Параметры:
 * - произвольный набор дат
 */
@Service
public class JPMaxLocalDateFunction implements JPFunction<LocalDate> {
  private ParserService parserService;

  @Autowired
  private void setParserService(ParserService parserService) {
    this.parserService = parserService;
  }

  @Override
  public LocalDate eval(Object... params) {
    if (params == null) {
      return null;
    }
    return Arrays.stream(params)
        .map(x -> parserService.parseTo(LocalDate.class, x))
        .filter(Objects::nonNull)
        .max(LocalDate::compareTo)
        .orElse(null);
  }
}
