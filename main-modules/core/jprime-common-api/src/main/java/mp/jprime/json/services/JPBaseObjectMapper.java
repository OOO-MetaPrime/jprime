package mp.jprime.json.services;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.lang.JPJsonNode;
import mp.jprime.lang.JPMap;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;
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

  public String toString(JPJsonNode jpJsonNode, String... paths) {
    if (jpJsonNode == null || paths == null || paths.length == 0) {
      return toString(jpJsonNode);
    }
    JsonNode node = jpJsonNode.toJsonNode();
    for (String path : paths) {
      node = node != null ? node.path(path) : null;
    }
    if (node == null || node.isMissingNode()) {
      return null;
    }
    return node.asText();
  }

  @JsonFilter("exceptFilter")
  private static class ExceptFilterMixIn {

  }

  public String toString(Object object, Collection<String> ignoreProps) {
    if (object == null) {
      return null;
    }
    if (ignoreProps == null || ignoreProps.isEmpty()) {
      return toString(object);
    }
    try {
      FilterProvider filterProvider = new SimpleFilterProvider()
          .addFilter("exceptFilter", SimpleBeanPropertyFilter.serializeAllExcept(new HashSet<>(ignoreProps)));
      ObjectWriter writer = getObjectMapper().copy()
          .addMixIn(Object.class, ExceptFilterMixIn.class)
          .writer(filterProvider);
      return writer.writeValueAsString(object);
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


  public JPJsonNode toJPJsonNode(JPMap map) {
    return map == null ? null : toJPJsonNode(map.toMap());
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

  public Map<String, Object> toMap(String value) {
    return toMap(toJPJsonNode(value));
  }

  public Map<String, Object> toMap(JPJsonNode value) {
    return value == null ? null : toObject(new TypeReference<>() {
    }, value.toString());
  }

  public <T> T toObject(TypeReference<T> to, Object value) {
    if (value == null) {
      return null;
    }
    if (to == null) {
      throw new IllegalArgumentException("Unset destination type <to> on call JPObjectMapper");
    }
    return toObject(to, toString(value));
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

  public void writeTo(OutputStream value, Object object) {
    try {
      getObjectMapper().writeValue(value, object);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }
}
