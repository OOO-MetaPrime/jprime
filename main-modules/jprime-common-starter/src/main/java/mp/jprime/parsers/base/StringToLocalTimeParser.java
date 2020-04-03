package mp.jprime.parsers.base;

import mp.jprime.json.format.DateTimeFormat;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
/**
 * String -> LocalTime
 */
@Service
public class StringToLocalTimeParser implements TypeParser<String, LocalTime> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public LocalTime parse(String value) {
    if (value == null) {
      return null;
    }
    if (value.length() > 19) {
      value = value.substring(11, 19);
    }
    return LocalTime.parse(value, DateTimeFormat.LOCAL_TIME_FORMAT);
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<String> getInputType() {
    return String.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<LocalTime> getOutputType() {
    return LocalTime.class;
  }
}
