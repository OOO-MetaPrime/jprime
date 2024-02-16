package mp.jprime.parsers.base;

import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPJsonString;
import mp.jprime.parsers.TypeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * {@link LinkedHashMap} -> {@link JPJsonString}
 */
@Service
public class LinkedHashMapToJPJsonStringParser implements TypeParser<LinkedHashMap, JPJsonString> {
  private static final Logger LOG = LoggerFactory.getLogger(LinkedHashMapToJPJsonStringParser.class);
  private JPJsonMapper jsonMapper;

  @Autowired
  private void setJsonMapper(JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  @Override
  public JPJsonString parse(LinkedHashMap value) {
    String str = null;
    try {
      str = value == null ? null : jsonMapper.toString(value);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return str == null ? null : JPJsonString.from(str);
  }

  @Override
  public Class<LinkedHashMap> getInputType() {
    return LinkedHashMap.class;
  }

  @Override
  public Class<JPJsonString> getOutputType() {
    return JPJsonString.class;
  }
}
