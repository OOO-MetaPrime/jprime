package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Timestamp -> Date
 */
@Service
public class TimestampToDateParser implements TypeParser<Timestamp, Date> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Date parse(Timestamp value) {
    return value != null ? new Date(value.getTime()) : null;
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<Timestamp> getInputType() {
    return Timestamp.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<Date> getOutputType() {
    return Date.class;
  }
}
