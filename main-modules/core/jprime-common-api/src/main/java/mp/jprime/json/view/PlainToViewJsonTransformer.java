package mp.jprime.json.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.lang.JPJsonNode;
import mp.jprime.parsers.ValueParser;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Трансформирует объект в формат универсального представления
 *
 * JSON универсального представления представляет из себя массив объектов
 * Если поле объекта имеет аннотацию {@link JsonViewProperty}
 * {
 *     "code": <имя поля или значение из аннотации {@link JsonProperty} если она есть>,
 *     "title": <value из {@link JsonViewProperty}>,
 *     "type": <код типа поля>,
 *     "value": <значение из поля>
 * }
 *
 * Маппинг типов
 * - простые типы
 * String.class = "string"
 * Boolean.class = "boolean"
 * Integer.class = "integer"
 * Long.class = "long"
 * Double.class = "double"
 * Date.class = "date"
 * LocalDate.class = "date"
 * LocalDateTime.class = "dateTime"
 * LocalTime.class = "time"
 * - коллекции
 * java.util.Collection и его наследники = "collection"
 * - в других случаях
 * составные объекты = "group"
 *
 * Если в классе объекта над методом есть аннотация {@link JsonViewValue} то вызвать этот метод и использовать значение,
 * в противном случае анализировать объект на аннотации {@link JsonViewCollection} и {@link JsonViewProperty}
 *
 * если над полем стоит аннотация {@link JsonViewCollection} значит это коллекция
 * сделать объект
 * {
 *     "code": <имя поля или значение из аннотации {@link JsonProperty} если она есть>,
 *     "length": <размер коллекции>,
 *     "title": <value из {@link JsonViewCollection}>,
 *     "type": "collection",
 *     "items": [ ...массив объектов коллекции... ]
 *   }
 *
 *  для составных объектов
 *  {
 *     "code": <имя поля или значение из аннотации {@link JsonProperty} если она есть>,
 *     "title": <value из {@link JsonViewProperty}>,
 *     "type": "group",
 *     "items": [ ...поля составного объекта или значение метода с аннотацией {@link JsonViewValue}... ]
 *   }
 */
public abstract class PlainToViewJsonTransformer {

  private static final JsonNodeFactory JSON_NODE_FACTORY = JsonNodeFactory.instance;

  public static JPJsonNode transformToViewJson(Object object) {
    return JPJsonNode.from(transform(object));
  }

  private static ArrayNode transform(Object object) {
    ArrayNode result = JSON_NODE_FACTORY.arrayNode();
    if (object == null) {
      return result;
    }
    Class<?> clazz = object.getClass();

    for (Field field : clazz.getDeclaredFields()) {
      field.setAccessible(true);
      try {
        Object value = field.get(object);
        if (value == null) {
          continue;
        }
        Annotation propertyAnnotation = field.getAnnotation(JsonViewProperty.class);
        Annotation collectionAnnotation = field.getAnnotation(JsonViewCollection.class);

        if (propertyAnnotation != null) {
          result.add(transformProperty(field, propertyAnnotation, value));
        } else if (collectionAnnotation != null) {
          result.add(transformCollection(field, collectionAnnotation, (Collection<?>) value));
        }
      } catch (IllegalAccessException e) {
        throw JPRuntimeException.wrapException("Error processing field " + field.getName() + " in " + clazz.getSimpleName(), e);
      }
    }

    return result;
  }

  private static ObjectNode transformProperty(Field field, Annotation propertyAnnotation, Object value) {
    JsonViewProperty property = (JsonViewProperty) propertyAnnotation;
    ObjectNode jsonObject = JSON_NODE_FACTORY.objectNode();

    JsonProperty jsonPropertyAnno = field.getAnnotation(JsonProperty.class);
    jsonObject.put(JsonViewPropertiesConst.CODE, jsonPropertyAnno != null ? jsonPropertyAnno.value() : field.getName());
    jsonObject.put(JsonViewPropertiesConst.TITLE, property.value());
    Object actualValue = value;
    if (!Void.class.equals(((JsonViewProperty) propertyAnnotation).toClass())) {
      actualValue = ValueParser.parseTo(((JsonViewProperty) propertyAnnotation).toClass(), value);
      Method valueMethod = getValueMethod(actualValue.getClass());
      if (valueMethod != null) {
        try {
          actualValue = valueMethod.invoke(actualValue);
        } catch (Exception e) {
          throw JPRuntimeException.wrapException("Error invoke value method " + valueMethod.getName() + " in " + actualValue.getClass().getSimpleName(), e);
        }
      }
    }
    if (JsonViewUtils.isSimpleType(actualValue.getClass())) {
      JsonViewUtils.putValue(jsonObject, actualValue);
    } else {
      jsonObject.put(JsonViewPropertiesConst.TYPE, "group");
      jsonObject.set(JsonViewPropertiesConst.ITEMS, transform(actualValue));
    }

    return jsonObject;
  }

  private static ObjectNode transformCollection(Field field, Annotation collectionAnnotation, Collection<?> collection) {
    JsonViewCollection collectionInfo = (JsonViewCollection) collectionAnnotation;
    ObjectNode jsonObject = JSON_NODE_FACTORY.objectNode();

    jsonObject.put(JsonViewPropertiesConst.CODE, field.getName());
    jsonObject.put(JsonViewPropertiesConst.LENGTH, collection.size());
    jsonObject.put(JsonViewPropertiesConst.TITLE, collectionInfo.value());
    jsonObject.put(JsonViewPropertiesConst.TYPE, "collection");

    ArrayNode items = JSON_NODE_FACTORY.arrayNode();
    if (!collection.isEmpty()) {
      Object firstObject = collection.iterator().next();
      if (JsonViewUtils.isSimpleType(firstObject.getClass())) {
        for (Object item : collection) {
          ObjectNode itemObject = JSON_NODE_FACTORY.objectNode();
          itemObject.put(JsonViewPropertiesConst.TITLE, collectionInfo.entity());
          JsonViewUtils.putValue(itemObject, item);
          items.add(itemObject);
        }
      } else {
        Method valueMethod = getValueMethod(firstObject.getClass());
        if (valueMethod != null) {
          for (Object item : collection) {
            Object v;
            try {
              v = valueMethod.invoke(item);
            } catch (Exception e) {
              throw JPRuntimeException.wrapException("Error invoke value method " + valueMethod.getName() + " in " + item.getClass().getSimpleName(), e);
            }
            if (v == null) {
              continue;
            }
            ObjectNode itemObject = JSON_NODE_FACTORY.objectNode();
            itemObject.put(JsonViewPropertiesConst.TITLE, collectionInfo.entity());
            JsonViewUtils.putValue(itemObject, v);
            items.add(itemObject);
          }
        } else {
          for (Object item : collection) {
            ObjectNode itemObject = JSON_NODE_FACTORY.objectNode();
            itemObject.put(JsonViewPropertiesConst.TITLE, collectionInfo.entity());
            itemObject.put(JsonViewPropertiesConst.TYPE, "group");
            itemObject.set(JsonViewPropertiesConst.ITEMS, transform(item));
            items.add(itemObject);
          }
        }
      }
    }

    jsonObject.set(JsonViewPropertiesConst.ITEMS, items);
    return jsonObject;
  }

  private static Method getValueMethod(Class<?> type) {
    for (Method method : type.getMethods()) {
      if (method.isAnnotationPresent(JsonViewValue.class)) {
        return method;
      }
    }
    return null;
  }
}