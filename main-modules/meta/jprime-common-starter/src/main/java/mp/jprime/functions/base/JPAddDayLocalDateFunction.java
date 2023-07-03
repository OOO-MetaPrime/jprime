package mp.jprime.functions.base;

import mp.jprime.functions.JPFunction;
import mp.jprime.parsers.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Добавить N дней
 * <p>
 * Параметры:
 * - дата
 * - количество дней для добавления
 */
@Service
public class JPAddDayLocalDateFunction implements JPFunction<LocalDate> {
  private ParserService parserService;

  @Autowired
  private void setParserService(ParserService parserService) {
    this.parserService = parserService;
  }

  @Override
  public LocalDate eval(Object... params) {
    if (params == null || params.length != 2) {
      throw new IllegalArgumentException();
    }
    LocalDate localDate = parserService.parseTo(LocalDate.class, params[0]);
    Long daysCount = parserService.parseTo(Long.class, params[1]);
    if (localDate == null || daysCount == null) {
      throw new IllegalArgumentException();
    }
    return localDate.plusDays(daysCount);
  }
}
