package mp.jprime.parsers.base;

import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPJsonNode;
import mp.jprime.parsers.BaseTypeParser;
import mp.jprime.parsers.exceptions.JPParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;

/**
 * JPJsonNode -> String
 */
@Service
public final class StringToJPJsonNodeParser extends BaseTypeParser<String, JPJsonNode> {
  private JPJsonMapper jsonMapper;

  @Autowired
  private void setJsonMapper(JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  @Override
  public JPJsonNode parse(String value) {
    JsonNode jsonNode;
    try {
      jsonNode = !StringUtils.hasText(value) ? null : jsonMapper.getObjectMapper().readTree(value);
    } catch (JacksonException e) {
      throw new JPParseException("jpJsonNode.parse", "Неверный формат");
    }
    return JPJsonNode.from(jsonNode);
  }

  @Override
  public Class<String> getInputType() {
    return String.class;
  }

  @Override
  public Class<JPJsonNode> getOutputType() {
    return JPJsonNode.class;
  }
}
