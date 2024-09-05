package mp.jprime.parsers.base;

import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPJsonNode;
import mp.jprime.lang.JPJsonString;
import mp.jprime.parsers.TypeParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * JsonString -> JPJsonNode
 */
@Service
public final class JsonStringToJPJsonNodeParser implements TypeParser<JPJsonString, JPJsonNode> {
  private JPJsonMapper jpJsonMapper;

  @Autowired
  private void setJPJsonMapper(JPJsonMapper jpJsonMapper) {
    this.jpJsonMapper = jpJsonMapper;
  }

  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public JPJsonNode parse(JPJsonString value) {
    try {
      return value == null ? null : JPJsonNode.from(jpJsonMapper.getObjectMapper().readTree(value.toString()));
    } catch (Exception e) {
      throw new JPRuntimeException(e.getMessage(), e);
    }
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<JPJsonString> getInputType() {
    return JPJsonString.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<JPJsonNode> getOutputType() {
    return JPJsonNode.class;
  }
}
