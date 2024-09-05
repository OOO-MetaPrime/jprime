package mp.jprime.parsers.base;

import mp.jprime.formats.DateFormat;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * LocalDate -> String
 */
@Service
public final class LocalDateToStringParser implements TypeParser<LocalDate, String> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public String parse(LocalDate value) {
    return value == null ? null : DateFormat.LOCAL_DATE_FORMAT.format(value);
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<LocalDate> getInputType() {
    return LocalDate.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<String> getOutputType() {
    return String.class;
  }
}
