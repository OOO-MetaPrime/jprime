package mp.jprime.parsers.base;

import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPJsonNode;
import mp.jprime.parsers.BaseTypeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * JPJsonNode -> String
 */
@Service
public final class JPJsonNodeToStringParser extends BaseTypeParser<JPJsonNode, String> {
  private static final Logger LOG = LoggerFactory.getLogger(JPJsonNodeToStringParser.class);
  private JPJsonMapper jsonMapper;

  @Autowired
  private void setJsonMapper(JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

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

  @Override
  public Class<JPJsonNode> getInputType() {
    return JPJsonNode.class;
  }

  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}
