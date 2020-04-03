package mp.jprime.parsers.base;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.parsers.ParserService;
import mp.jprime.parsers.TypeParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * JPId -> BigInteger
 */
@Service
public class JPIdToBigIntegerParser implements TypeParser<JPId, BigInteger> {
  private ParserService service;

  @Autowired
  private void setService(ParserService service) {
    this.service = service;
  }

  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public BigInteger parse(JPId value) {
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
  public Class<BigInteger> getOutputType() {
    return BigInteger.class;
  }
}
