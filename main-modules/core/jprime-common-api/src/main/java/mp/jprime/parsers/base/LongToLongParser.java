package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * Long -> Long
 */
@Service
public final class LongToLongParser implements TypeParser<Long, Long> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Long parse(Long value) {
    return value;
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<Long> getInputType() {
    return Long.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<Long> getOutputType() {
    return Long.class;
  }
}