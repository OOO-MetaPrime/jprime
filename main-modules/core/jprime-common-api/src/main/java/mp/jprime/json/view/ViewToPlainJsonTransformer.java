package mp.jprime.json.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPJsonNode;
import mp.jprime.lang.JPJsonString;
import mp.jprime.parsers.ValueParser;

import java.io.IOException;
import java.util.Objects;

/**
 * Трансформирует JSON формата универсального представления в обычный JSON
 *
 * если type равен group значит в items объекты с полями другого объекта
 * если type равен collection значит это массив в items коллекция других объектов
 * если у объекта внутри type равен string, boolean, integer, long, double, date, dateTime, time то это просто объекты коллекции
 */
public abstract class ViewToPlainJsonTransformer {

  /**
   * Трансформирует JSON формата универсального представления в обычный JSON
   * @param viewJson JSON формата универсального представления
   * @return обычный JSON
   */
  public static JPJsonString toPlainJson(JPJsonString viewJson) {
    try {
      ArrayNode inputArray = (ArrayNode) JPJsonMapper.getMapper().readTree(viewJson.toString());
      ObjectNode result = transformJson(inputArray);
      return JPJsonString.from(JPJsonMapper.getMapper().writeValueAsString(result));
    } catch (IOException e) {
      throw JPRuntimeException.wrapException("Error reading view JSON", e);
    }
  }

  /**
   * Трансформирует JSON формата универсального представления в обычный JSON
   * @param viewJson JSON формата универсального представления
   * @return обычный JSON
   */
  public static JPJsonNode toPlainJson(JPJsonNode viewJson) {
    try {
      ArrayNode inputArray = (ArrayNode) JPJsonMapper.getMapper().readTree(viewJson.toString());
      ObjectNode result = transformJson(inputArray);
      return JPJsonNode.from(result);
    } catch (IOException e) {
      throw JPRuntimeException.wrapException("Error reading view JSON", e);
    }
  }

  private static ObjectNode transformJson(ArrayNode inputArray) {
    ObjectNode transformed = JPJsonMapper.getMapper().createObjectNode();

    for (JsonNode item : inputArray) {
      String code = Objects.toString(item.get("code").asText(), null);
      String type = Objects.toString(item.get("type").asText(), null);

      if ("group".equals(type)) {
        ArrayNode groupItems = (ArrayNode) item.get("items");
        transformed.set(code, transformJson(groupItems));
      } else if ("collection".equals(type)) {
        ArrayNode collectionItems = (ArrayNode) item.get("items");
        transformed.set(code, processCollection(collectionItems));
      } else {
        JsonNode value = item.get("value");
        transformed.set(code, JPJsonMapper.getMapper().valueToTree(processValue(type, value)));
      }
    }

    return transformed;
  }

  private static Object processValue(String type, JsonNode value) {
    if (value == null || value.isNull()) {
      return null;
    }
    return switch (type) {
      case "string", "date", "dateTime", "time" -> value.asText();
      case "boolean" -> "Да".equals(value.asText());
      case "integer" -> ValueParser.parseTo(Integer.class, value.asText());
      case "long" -> ValueParser.parseTo(Long.class, value.asText());
      case "double" -> ValueParser.parseTo(Double.class, value.asText());
      default -> value;
    };
  }

  private static ArrayNode processCollection(ArrayNode collectionItems) {
    ArrayNode result = JPJsonMapper.getMapper().createArrayNode();
    if (collectionItems.isEmpty()) {
      return result;
    }

    JsonNode firstItem = collectionItems.get(0);
    String type = firstItem.get("type").asText();

    if ("group".equals(type)) {
      for (JsonNode groupItem : collectionItems) {
        ArrayNode groupItems = (ArrayNode) groupItem.get("items");
        result.add(transformJson(groupItems));
      }
    } else {
      for (JsonNode simpleItem : collectionItems) {
        result.add(simpleItem.get("value"));
      }
    }
    return result;
  }
}
