package mp.jprime.lang;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Обёртка {@link JsonNode}
 */
public class JPJsonNode implements Comparable<JPJsonNode> {
  private final JsonNode jsonNode;

  private JPJsonNode(JsonNode jsonNode) {
    this.jsonNode = jsonNode;
  }

  /**
   * Создать обёртку из {@link JsonNode}
   *
   * @param jsonNode {@code JsonNode}
   * @return Обёртка
   */
  public static JPJsonNode from(JsonNode jsonNode) {
    return new JPJsonNode(jsonNode);
  }

  /**
   * Получить {@link JsonNode} из обёртки
   *
   * @return {@code JsonNode}
   */
  public JsonNode toJsonNode() {
    return jsonNode;
  }

  @Override
  public String toString() {
    return jsonNode.toString();
  }

  @Override
  public int compareTo(JPJsonNode jpJsonNode) {
    if (jsonNode == null) {
      return -1;
    }
    if (jpJsonNode.toJsonNode() == null) {
      return 1;
    }
    return jsonNode.equals(jpJsonNode.toJsonNode()) ? 0 : 1;
  }

  public static boolean isEquals(JPJsonNode v1, JPJsonNode v2) {
    if (v1 == v2) {
      return true;
    }
    if (v1 == null || v2 == null) {
      return false;
    }
    return isEquals(v1.toJsonNode(), v2.toJsonNode());
  }

  public static boolean isEquals(JsonNode n1, JsonNode n2) {
    if (n1 == n2) {
      return true;
    }
    if (n1 == null || n2 == null) {
      return false;
    }

    if (n1.isObject() && n2.isObject()) {
      if (n1.size() != n2.size()) {
        return false;
      }
      // Проверяем, что все поля из n1 есть в n2 и совпадают
      var iter = n1.fieldNames();
      while (iter.hasNext()) {
        String fieldName = iter.next();
        JsonNode value1 = n1.get(fieldName);
        JsonNode value2 = n2.get(fieldName);
        if (!isEquals(value1, value2)) {
          return false;
        }
      }
      return true;
    } else if (n1.isArray() && n2.isArray()) {
      // Для массивов порядок элементов важен
      if (n1.size() != n2.size()) {
        return false;
      }
      for (int i = 0; i < n1.size(); i++) {
        if (!isEquals(n1.get(i), n2.get(i))) {
          return false;
        }
      }
      return true;
    } else {
      if (n1.asText().equals(n2.asText())) {
        return true;
      }
      return n1.equals(n2);
    }
  }
}
