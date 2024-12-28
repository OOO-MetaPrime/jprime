package mp.jprime.parsers.base;

import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPJsonNode;
import mp.jprime.parsers.TypeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * {@link LinkedHashMap} -> {@link JPJsonNode}
 */
@Service
public final class LinkedHashMapToJPJsonNodeParser implements TypeParser<LinkedHashMap, JPJsonNode> {
  private static final Logger LOG = LoggerFactory.getLogger(LinkedHashMapToJPJsonNodeParser.class);
  private JPJsonMapper jsonMapper;

  @Autowired
  private void setJsonMapper(JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  @Override
  public JPJsonNode parse(LinkedHashMap value) {
    try {
      String str = value == null ? null : jsonMapper.toString(value);
      return JPJsonNode.from(jsonMapper.getObjectMapper().readTree(str));
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public Class<LinkedHashMap> getInputType() {
    return LinkedHashMap.class;
  }

  @Override
  public Class<JPJsonNode> getOutputType() {
    return JPJsonNode.class;
  }
}
