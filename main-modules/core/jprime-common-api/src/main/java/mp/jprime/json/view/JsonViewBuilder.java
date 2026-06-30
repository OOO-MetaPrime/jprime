package mp.jprime.json.view;

import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPJsonNode;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

import java.util.*;

/**
 * Билдер универального представления
 */
public final class JsonViewBuilder {

  private static final JsonNodeFactory JSON_NODE_FACTORY = JsonNodeFactory.instance;

  private final ArrayNode root;
  private ArrayNode step;

  private JsonViewBuilder(ArrayNode json) {
    this.root = json;
    this.step = json;
  }

  public static JsonViewBuilder newBuilder() {
    return new JsonViewBuilder(JSON_NODE_FACTORY.arrayNode());
  }

  /**
   * Дополняет представление JSON коллекцией
   *
   * @param title  заголовок коллекции элементов
   * @param entity наименование элемента коллекции
   * @param c      коллекция элементов
   */
  public JsonViewBuilder appendCollection(String title, String entity, Collection<?> c) {
    return appendCollection(null, title, entity, c);
  }

  /**
   * Дополняет представление JSON коллекцией
   *
   * @param code   код элемента
   * @param title  заголовок коллекции элементов
   * @param entity наименование элемента коллекции
   * @param c      коллекция элементов
   */
  public JsonViewBuilder appendCollection(String code, String title, String entity, Collection<?> c) {
    if (c == null || c.isEmpty()) {
      return this;
    }
    ObjectNode group = JSON_NODE_FACTORY.objectNode();
    if (code != null) {
      group.put(JsonViewPropertiesConst.CODE, code);
    }
    if (title != null) {
      group.put(JsonViewPropertiesConst.TITLE, title);
    }
    group.put(JsonViewPropertiesConst.TYPE, "collection");
    ArrayNode items = group.putArray(JsonViewPropertiesConst.ITEMS);
    group.put(JsonViewPropertiesConst.LENGTH, c.size());
    for (Object o : c) {
      ObjectNode item = JSON_NODE_FACTORY.objectNode();
      item.put(JsonViewPropertiesConst.TYPE, "group");
      if (entity != null) {
        item.put(JsonViewPropertiesConst.TITLE, entity);
      }
      if (o instanceof JsonViewAware) {
        ArrayNode prev = this.step;
        this.step = item.putArray(JsonViewPropertiesConst.ITEMS);
        ((JsonViewAware) o).toJson(this);
        this.step = prev;
      } else {
        JsonViewUtils.putValue(item, o);
      }
      items.add(item);
    }
    step.add(group);
    return this;
  }

  /**
   * Дополняет представление JSON
   *
   * @param title наименование элемента
   * @param o     объект
   */
  public JsonViewBuilder append(String title, Object o) {
    return append(null, title, o);
  }

  /**
   * Дополняет представление JSON
   *
   * @param code  код элемента
   * @param title наименование элемента
   * @param o     объект
   */
  public JsonViewBuilder append(String code, String title, Object o) {
    if (o == null) {
      return this;
    }
    ObjectNode item = JSON_NODE_FACTORY.objectNode();
    item.put(JsonViewPropertiesConst.CODE, code);
    item.put(JsonViewPropertiesConst.TITLE, title);
    if (o instanceof JsonViewAware) {
      item.put(JsonViewPropertiesConst.TYPE, "group");
      ArrayNode prev = this.step;
      this.step = item.putArray(JsonViewPropertiesConst.ITEMS);
      ((JsonViewAware) o).toJson(this);
      this.step = prev;
    } else {
      JsonViewUtils.putValue(item, o);
    }
    step.add(item);
    return this;
  }


  /**
   * Дополняет представление JSON тескстовым объектом
   *
   * @param title  наименование элемента
   * @param s      строка
   * @param length длина строки
   * @return
   */
  public JsonViewBuilder appendString(String title, String s, int length) {
    return appendString(null, title, s, length);
  }

  /**
   * Дополняет представление JSON тескстовым объектом
   *
   * @param code   код элемента
   * @param title  наименование элемента
   * @param s      строка
   * @param length длина строки
   * @return
   */
  public JsonViewBuilder appendString(String code, String title, String s, int length) {
    if (s == null) {
      return this;
    }
    ObjectNode item = JSON_NODE_FACTORY.objectNode();
    if (code != null) {
      item.put(JsonViewPropertiesConst.CODE, code);
    }
    item.put(JsonViewPropertiesConst.TITLE, title);
    item.put(JsonViewPropertiesConst.TYPE, "string");
    item.put(JsonViewPropertiesConst.VALUE, s);
    item.put(JsonViewPropertiesConst.LENGTH, length);
    step.add(item);
    return this;
  }

  @Override
  public String toString() {
    try {
      return JPJsonMapper.getMapper().writeValueAsString(root);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException("Error write json-view", e);
    }
  }

  public JPJsonNode toJsonNode() {
    return JPJsonNode.from(root);
  }
}
