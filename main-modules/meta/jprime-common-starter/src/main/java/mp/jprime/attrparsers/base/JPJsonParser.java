package mp.jprime.attrparsers.base;

import com.fasterxml.jackson.databind.JsonNode;
import mp.jprime.attrparsers.AttrTypeParser;
import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPJsonNode;
import mp.jprime.lang.JPJsonString;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.beans.JPType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Реализация парсера {@link JPType#JSON}
 */
@Service
public class JPJsonParser implements AttrTypeParser<JPJsonNode> {
  private static final JPType JP_TYPE = JPType.JSON;

  private JPJsonMapper jsonMapper;

  @Autowired
  private void setJsonMapper(JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  @Override
  public JPJsonNode parse(JPAttr jpAttr, JPAttrData data) {
    if (data == null || jpAttr == null || jpAttr.getValueType() != JP_TYPE) {
      return null;
    }
    Object attrValue = data.get(jpAttr);
    if (attrValue == null) {
      return null;
    }

    try {
      if (attrValue instanceof JsonNode) {
        return JPJsonNode.from((JsonNode) attrValue);
      } else {
        return parse(jpAttr, attrValue);
      }
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new JPRuntimeException(e.getMessage(), e);
    }
  }

  @Override
  public JPJsonNode parse(JPAttr jpAttr, Object attrValue) {
    if (jpAttr == null || jpAttr.getValueType() != JP_TYPE) {
      return null;
    }

    try {
      if (attrValue instanceof String) {
        return JPJsonNode.from(jsonMapper.toJsonNode((String) attrValue));
      } else if (attrValue instanceof JPJsonString) {
        return JPJsonNode.from(jsonMapper.toJsonNode(attrValue.toString()));
      } else {
        return jsonMapper.toJPJsonNode(attrValue);
      }
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new JPRuntimeException(e.getMessage(), e);
    }
  }

  @Override
  public void fill(JPAttr jpAttr, JPJsonNode attrValue, JPMutableData data) {
    if (data == null || jpAttr == null || jpAttr.getValueType() != JP_TYPE) {
      return;
    }
    data.put(jpAttr.getCode(), attrValue);
  }

  @Override
  public JPType getJPType() {
    return JP_TYPE;
  }

  @Override
  public Class<JPJsonNode> getOutputType() {
    return JPJsonNode.class;
  }
}
