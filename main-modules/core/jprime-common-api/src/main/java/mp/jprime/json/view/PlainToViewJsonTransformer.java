package mp.jprime.json.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.formats.DateFormat;
import mp.jprime.lang.JPJsonNode;
import mp.jprime.parsers.ValueParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

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

  private static final DateTimeFormatter LOCAL_DATETIME_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
  private static final DateTimeFormatter LOCAL_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
  private static final DateTimeFormatter LOCAL_TIME_FORMAT = DateTimeFormatter.ISO_LOCAL_TIME;

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
    jsonObject.put("code", jsonPropertyAnno != null ? jsonPropertyAnno.value() : field.getName());
    jsonObject.put("title", property.value());
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
    jsonObject.put("type", mapType(actualValue.getClass()));
    if (isSimpleType(actualValue.getClass())) {
      actualValue = resolveValue(actualValue);
      if (actualValue instanceof String typeValue) {
        jsonObject.put("value", typeValue);
      } else if (actualValue instanceof Integer typeValue) {
        jsonObject.put("value", typeValue);
      } else if (actualValue instanceof Long typeValue) {
        jsonObject.put("value", typeValue);
      } else if (actualValue instanceof Double typeValue) {
        jsonObject.put("value", typeValue);
      } else if (actualValue instanceof BigInteger typeValue) {
        jsonObject.put("value", typeValue);
      } else if (actualValue instanceof BigDecimal typeValue) {
        jsonObject.put("value", typeValue);
      } else {
        jsonObject.put("value", Objects.toString(actualValue, null));
      }
    } else {
      jsonObject.set("items", transform(actualValue));
    }

    return jsonObject;
  }

  private static ObjectNode transformCollection(Field field, Annotation collectionAnnotation, Collection<?> collection) {
    JsonViewCollection collectionInfo = (JsonViewCollection) collectionAnnotation;
    ObjectNode jsonObject = JSON_NODE_FACTORY.objectNode();

    jsonObject.put("code", field.getName());
    jsonObject.put("length", collection.size());
    jsonObject.put("title", collectionInfo.value());
    jsonObject.put("type", "collection");

    ArrayNode items = JSON_NODE_FACTORY.arrayNode();
    if (!collection.isEmpty()) {
      Object firstObject = collection.iterator().next();
      if (isSimpleType(firstObject.getClass())) {
        String typeName = mapType(firstObject.getClass());
        for (Object item : collection) {
          ObjectNode itemObject = JSON_NODE_FACTORY.objectNode();
          itemObject.put("title", collectionInfo.entity());
          itemObject.put("type", typeName);
          itemObject.put("value", Objects.toString(resolveValue(item), null));
          items.add(itemObject);
        }
      } else {
        Method valueMethod = getValueMethod(firstObject.getClass());
        if (valueMethod != null) {
          String typeName = null;
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
            if (typeName == null) {
              typeName = mapType(v.getClass());
            }
            ObjectNode itemObject = JSON_NODE_FACTORY.objectNode();
            itemObject.put("title", collectionInfo.entity());
            itemObject.put("type", typeName);
            itemObject.put("value", resolveValue(v).toString());
            items.add(itemObject);
          }
        } else {
          for (Object item : collection) {
            ObjectNode itemObject = JSON_NODE_FACTORY.objectNode();
            itemObject.put("title", collectionInfo.entity());
            itemObject.put("type", "group");
            itemObject.set("items", transform(item));
            items.add(itemObject);
          }
        }
      }
    }

    jsonObject.set("items", items);
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

  private static boolean isSimpleType(Class<?> type) {
    return type.equals(String.class) || type.equals(UUID.class)  || type.equals(Boolean.class)
        || type.equals(Integer.class) || type.equals(BigInteger.class)
        || type.equals(Long.class) || type.equals(Double.class) || type.equals(BigDecimal.class)
        || java.time.LocalDate.class.equals(type) || java.time.LocalDateTime.class.equals(type) || java.time.LocalTime.class.equals(type);
  }

  private static String mapType(Class<?> type) {
    if (type.equals(String.class) || UUID.class.equals(type)) return "string";
    if (type.equals(Boolean.class)) return "boolean";
    if (type.equals(Integer.class) || BigInteger.class.equals(type)) return "integer";
    if (type.equals(Long.class)) return "long";
    if (type.equals(Double.class) || BigDecimal.class.equals(type)) return "double";
    if (java.time.LocalDate.class.equals(type)) return "date";
    if (java.time.LocalDateTime.class.equals(type)) return "dateTime";
    if (java.time.LocalTime.class.equals(type)) return "time";
    if (Collection.class.isAssignableFrom(type)) return "collection";
    return "group";
  }

  private static Object resolveValue(Object value) {
    if (value instanceof UUID uuid) {
      return uuid.toString();
    } else if (value instanceof LocalDate localDate) {
      return localDate.format(LOCAL_DATE_FORMAT);
    } else if (value instanceof LocalDateTime localDateTime) {
      return localDateTime.format(LOCAL_DATETIME_FORMAT);
    } else if (value instanceof LocalTime localTime) {
      return localTime.format(LOCAL_TIME_FORMAT);
    } else if (value instanceof Boolean aBoolean) {
      return aBoolean ? "Да" : "Нет";
    }
    return value;
  }
}