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
}
