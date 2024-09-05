package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Date -> Timestamp
 */
@Service
public final class DateToTimestampParser implements TypeParser<Date, Timestamp> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Timestamp parse(Date value) {
    return value != null ? new Timestamp(value.getTime()) : null;
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<Date> getInputType() {
    return Date.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<Timestamp> getOutputType() {
    return Timestamp.class;
  }
}
