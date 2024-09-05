package mp.jprime.parsers.base;

import mp.jprime.parsers.ParserService;
import mp.jprime.parsers.ParserServiceAware;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Double -> BigDecimal
 */
@Service
public final class DoubleToBigDecimalParser implements TypeParser<Double, BigDecimal>, ParserServiceAware {
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
  public BigDecimal parse(Double value) {
    return value != null ? new BigDecimal(service.parseTo(String.class, value)) : null;
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<Double> getInputType() {
    return Double.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<BigDecimal> getOutputType() {
    return BigDecimal.class;
  }
}