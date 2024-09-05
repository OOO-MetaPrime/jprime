package mp.jprime.json.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.lang.JPJsonNode;

import java.io.InputStream;
import java.util.Map;

/**
 * Общая логика JP*Mapper
 */
public abstract class JPBaseObjectMapper {
  /**
   * Возвращает ObjectMapper
   *
   * @return ObjectMapper
   */
  abstract public ObjectMapper getObjectMapper();

  public String toString(Object object) {
    if (object == null) {
      return null;
    }
    try {
      return getObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  public JPJsonNode toJPJsonNode(InputStream is) {
    JsonNode node = toJsonNode(is);
    return node != null ? JPJsonNode.from(node) : null;
  }

  public JPJsonNode toJPJsonNode(String value) {
    JsonNode node = toJsonNode(value);
    return node != null ? JPJsonNode.from(node) : null;
  }

  public JsonNode toJsonNode(InputStream is) {
    if (is == null) {
      return null;
    }
    try {
      return getObjectMapper().readTree(is);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  public JsonNode toJsonNode(String value) {
    if (value == null) {
      return null;
    }
    try {
      return getObjectMapper().readTree(value);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  public <T> T toObject(Class<T> to, String value) {
    if (value == null) {
      return null;
    }
    if (to == null) {
      throw new IllegalArgumentException("Unset destination type <to> on call JPObjectMapper");
    }
    try {
      return getObjectMapper().readValue(value, to);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  public <T> T toObject(Class<T> to, JPJsonNode value) {
    return value == null ? null : toObject(to, value.toJsonNode());
  }

  public <T> T toObject(Class<T> to, JsonNode value) {
    if (value == null) {
      return null;
    }
    if (to == null) {
      throw new IllegalArgumentException("Unset destination type <to> on call JPObjectMapper");
    }
    try {
      return getObjectMapper().treeToValue(value, to);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  public JPJsonNode toJPJsonNode(Object value) {
    if (value == null) {
      return null;
    }
    try {
      return JPJsonNode.from(getObjectMapper().valueToTree(value));
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  public Map<String, Object> toMap(String value) {
    return toMap(toJPJsonNode(value));
  }

  public Map<String, Object> toMap(JPJsonNode value) {
    return value == null ? null : toObject(new TypeReference<>() {
    }, value.toString());
  }

  public <T> T toObject(TypeReference<T> to, String value) {
    if (value == null) {
      return null;
    }
    if (to == null) {
      throw new IllegalArgumentException("Unset destination type <to> on call JPObjectMapper");
    }
    try {
      return getObjectMapper().readValue(value, to);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  public <T> T toObject(Class<T> to, InputStream value) {
    if (value == null) {
      return null;
    }
    if (to == null) {
      throw new IllegalArgumentException("Unset destination type <to> on call JPObjectMapper");
    }
    try {
      return getObjectMapper().readValue(value, to);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }
}
