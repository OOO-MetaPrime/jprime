package mp.jprime.parsers.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPJsonNode;
import mp.jprime.parsers.TypeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * JPJsonNode -> String
 */
@Service
public class StringToJPJsonNodeParser implements TypeParser<String, JPJsonNode> {
  private static final Logger LOG = LoggerFactory.getLogger(StringToJPJsonNodeParser.class);
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
  public JPJsonNode parse(String value) {
    JsonNode jsonNode = null;
    try {
      jsonNode = !StringUtils.hasText(value) ? null :
          jsonMapper.getObjectMapper().readTree(value);
    } catch (JsonProcessingException e) {
      LOG.error(e.getMessage(), e);
    }
    return JPJsonNode.from(jsonNode);
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  @Override
  public Class<String> getInputType() {
    return String.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  @Override
  public Class<JPJsonNode> getOutputType() {
    return JPJsonNode.class;
  }
}
