package mp.jprime.json.view;

import tools.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;

import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * Функции универсального представления
 */
public final class JsonViewUtils {

  private JsonViewUtils() {
  }

  private static final Map<Class<?>, String> SIMPLE_TYPES;
  private static final Map<Class<?>, BiConsumer<ObjectNode, Object>> HANDLERS;

  private static final DateTimeFormatter LOCAL_DATETIME_FORMAT = new DateTimeFormatterBuilder()
      .parseCaseInsensitive()
      .append(DateTimeFormatter.ISO_LOCAL_DATE)
      .appendLiteral('T')
      .appendValue(ChronoField.HOUR_OF_DAY, 2)
      .appendLiteral(':')
      .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
      .appendLiteral(':')
      .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
      .optionalStart()
      .appendPattern(".SSS")
      .optionalEnd()
      .optionalStart()
      .appendPattern("Z")
      .optionalEnd()
      .toFormatter()
      .withZone(ZoneId.systemDefault());

  private static final DateTimeFormatter LOCAL_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
  private static final DateTimeFormatter LOCAL_TIME_FORMAT = DateTimeFormatter.ISO_LOCAL_TIME;

  static {
    Map<Class<?>, String> map = new HashMap<>();
    map.put(String.class, "string");
    map.put(UUID.class, "string");
    map.put(Boolean.class, "boolean");
    map.put(Integer.class, "integer");
    map.put(BigInteger.class, "integer");
    map.put(Long.class, "long");
    map.put(Double.class, "double");
    map.put(BigDecimal.class, "double");
    map.put(Date.class, "date");
    map.put(LocalDate.class, "date");
    map.put(LocalDateTime.class, "dateTime");
    map.put(LocalTime.class, "time");
    SIMPLE_TYPES = Map.copyOf(map);

    Map<Class<?>, BiConsumer<ObjectNode, Object>> h = new HashMap<>();
    h.put(String.class, (n, v) -> n.put(JsonViewPropertiesConst.VALUE, (String) v));
    h.put(UUID.class, (n, v) -> n.put(JsonViewPropertiesConst.VALUE, v.toString()));
    h.put(Integer.class, (n, v) -> n.put(JsonViewPropertiesConst.VALUE, (Integer) v));
    h.put(Long.class, (n, v) -> n.put(JsonViewPropertiesConst.VALUE, (Long) v));
    h.put(Double.class, (n, v) -> n.put(JsonViewPropertiesConst.VALUE, (Double) v));
    h.put(BigInteger.class, (n, v) -> n.put(JsonViewPropertiesConst.VALUE, (BigInteger) v));
    h.put(BigDecimal.class, (n, v) -> n.put(JsonViewPropertiesConst.VALUE, (BigDecimal) v));
    h.put(Boolean.class, (n, v) -> n.put(JsonViewPropertiesConst.VALUE, (Boolean) v ? "Да" : "Нет"));
    h.put(Date.class, (n, v) -> {
      LocalDate ld = ((Date) v).toInstant()
          .atZone(ZoneId.systemDefault())
          .toLocalDate();
      n.put(JsonViewPropertiesConst.VALUE, ld.format(LOCAL_DATE_FORMAT));
    });
    h.put(LocalDate.class, (n, v) -> n.put(JsonViewPropertiesConst.VALUE, ((LocalDate) v).format(LOCAL_DATE_FORMAT)));
    h.put(LocalDateTime.class, (n, v) -> n.put(JsonViewPropertiesConst.VALUE, ((LocalDateTime) v).format(LOCAL_DATETIME_FORMAT)));
    h.put(LocalTime.class, (n, v) -> n.put(JsonViewPropertiesConst.VALUE, ((LocalTime) v).format(LOCAL_TIME_FORMAT)));
    HANDLERS = Map.copyOf(h);
  }

  public static boolean isSimpleType(Class<?> cls) {
    return SIMPLE_TYPES.containsKey(cls);
  }

  public static void putValue(ObjectNode item, Object o) {
    if (o == null) {
      item.put(JsonViewPropertiesConst.TYPE, "unknown");
      item.putNull(JsonViewPropertiesConst.VALUE);
      return;
    }

    // Тип по умолчанию — string
    String type = SIMPLE_TYPES.getOrDefault(o.getClass(), SIMPLE_TYPES.get(String.class));
    item.put(JsonViewPropertiesConst.TYPE, type);

    // Поищем handler по точному классу
    BiConsumer<ObjectNode, Object> handler = HANDLERS.get(o.getClass());

    if (handler != null) {
      handler.accept(item, o);
      return;
    }

    item.put(JsonViewPropertiesConst.VALUE, o.toString());
  }
}
