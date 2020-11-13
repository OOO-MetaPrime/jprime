package mp.jprime.parsers.base;

import mp.jprime.formats.DateFormat;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

/**
 * LocalTime -> String
 */
@Service
public class LocalTimeToStringParser implements TypeParser<LocalTime, String> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public String parse(LocalTime value) {
    return value == null ? null : DateFormat.LOCAL_TIME_FORMAT.format(value);
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<LocalTime> getInputType() {
    return LocalTime.class;
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
