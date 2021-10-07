package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * Timestamp -> LocalDate
 */
@Service
public class TimestampToLocalDateParser implements TypeParser<Timestamp, LocalDate> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public LocalDate parse(Timestamp value) {
    return value != null ? value.toLocalDateTime().toLocalDate() : null;
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
  public Class<LocalDate> getOutputType() {
    return LocalDate.class;
  }
}
