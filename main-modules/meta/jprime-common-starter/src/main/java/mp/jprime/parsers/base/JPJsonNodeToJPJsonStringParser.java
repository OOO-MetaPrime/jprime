package mp.jprime.parsers.base;

import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPJsonNode;
import mp.jprime.lang.JPJsonString;
import mp.jprime.parsers.TypeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * {@link JPJsonNode} -> {@link JPJsonString}
 */
@Service
public class JPJsonNodeToJPJsonStringParser implements TypeParser<JPJsonNode, JPJsonString> {
  private static final Logger LOG = LoggerFactory.getLogger(JPJsonNodeToJPJsonStringParser.class);
  private JPJsonMapper jsonMapper;

  @Autowired
  private void setJsonMapper(JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  @Override
  public JPJsonString parse(JPJsonNode value) {
    String str = null;
    try {
      str = value == null ? null : jsonMapper.toString(value.toJsonNode());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return str == null ? null : JPJsonString.from(str);
  }

  @Override
  public Class<JPJsonNode> getInputType() {
    return JPJsonNode.class;
  }

  @Override
  public Class<JPJsonString> getOutputType() {
    return JPJsonString.class;
  }
}
