package mp.jprime.parsers.base;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.parsers.ParserService;
import mp.jprime.parsers.ParserServiceAware;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * JPId -> Long
 */
@Service
public class JPIdToLongParser implements TypeParser<JPId, Long>, ParserServiceAware {
  private ParserService service;

  @Override
  public void setParserService(ParserService service) {
    this.service = service;
  }

  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Long parse(JPId value) {
    return value == null ? null : service.parseTo(getOutputType(), value.getId());
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<JPId> getInputType() {
    return JPId.class;
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
