package mp.jprime.parsers.base;

import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPJsonNode;
import mp.jprime.parsers.TypeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * JPJsonNode -> String
 */
@Service
public class JPJsonNodeToStringParser implements TypeParser<JPJsonNode, String> {
  private static final Logger LOG = LoggerFactory.getLogger(JPJsonNodeToStringParser.class);
  private JPJsonMapper jsonMapper;

  @Autowired
  private void setJsonMapper(JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  @Override
  public String parse(JPJsonNode value) {
    String str = null;
    try {
      str = value == null ? null : jsonMapper.toString(value.toJsonNode());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return str;
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  @Override
  public Class<JPJsonNode> getInputType() {
    return JPJsonNode.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}
