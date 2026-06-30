package mp.jprime.json.modules;

import mp.jprime.json.beans.*;
import mp.jprime.lang.*;
import mp.jprime.parsers.ValueParser;
import mp.jprime.xml.modules.JPObjectMapperXmlExpander;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.cfg.MapperBuilder;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.deser.std.StdScalarDeserializer;
import tools.jackson.databind.module.SimpleModule;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Подключение базовых обработчиков
 */
@Service
public final class JPObjectMapperCommonExpander implements JPObjectMapperExpander, JPObjectMapperXmlExpander {
  public final static long MAX_SAFE_INTEGER = 0x000FFFFFFFFFFFFFL * 2 + 1;
  public final static long MIN_SAFE_INTEGER = -1 * MAX_SAFE_INTEGER;

  public final static BigDecimal MAX_SAFE_BIGDECIMAL = BigDecimal.valueOf(MAX_SAFE_INTEGER);
  public final static BigDecimal MIN_SAFE_BIGDECIMAL = BigDecimal.valueOf(MIN_SAFE_INTEGER);

  public final static BigInteger MAX_SAFE_BIGINTEGER = BigInteger.valueOf(MAX_SAFE_INTEGER);
  public final static BigInteger MIN_SAFE_BIGINTEGER = BigInteger.valueOf(MIN_SAFE_INTEGER);

  @Override
  public void expand(MapperBuilder<?, ?> builder) {
    SimpleModule module = new SimpleModule()
        .addDeserializer(String.class, new StdScalarDeserializer<>(String.class) {
          @Override
          public String deserialize(JsonParser jsonParser, DeserializationContext ctx) {
            return jsonParser.getValueAsString();
          }
        })
        // String to Double
        .addDeserializer(Double.class, new StdDeserializer<>(Double.class) {
          @Override
          public Double deserialize(JsonParser p, DeserializationContext ctxt) {
            String value = p.getString();
            value = value != null ? value.trim() : null;
            if (value == null || value.isEmpty()) {
              return null;
            }
            return ValueParser.parseTo(Double.class, value);
          }
        })
        // String to JsonString
        .addDeserializer(JPJsonString.class, new StdDeserializer<>(JPJsonString.class) {
          @Override
          public JPJsonString deserialize(JsonParser p, DeserializationContext ctxt) {
            return JPJsonString.from(p.readValueAsTree().toString());
          }
        })
        // String to XmlString
        .addDeserializer(JPXmlString.class, new StdDeserializer<>(JPXmlString.class) {
          @Override
          public JPXmlString deserialize(JsonParser p, DeserializationContext ctxt) {
            return JPXmlString.from(p.readValueAsTree().toString());
          }
        })
        // String to JPSimpleFraction
        .addDeserializer(JPSimpleFraction.class, new StdDeserializer<>(JPSimpleFraction.class) {
          @Override
          public JPSimpleFraction deserialize(JsonParser p, DeserializationContext ctxt) {
            JsonSimpleFraction fraction = ctxt.readValue(p, JsonSimpleFraction.class);
            return JPSimpleFraction.of(fraction.isPositive(), fraction.getInteger(), fraction.getNumerator(), fraction.getDenominator());
          }
        })
        // String to IntegerRange
        .addDeserializer(JPIntegerRange.class, new StdDeserializer<>(JPIntegerRange.class) {
          @Override
          public JPIntegerRange deserialize(JsonParser p, DeserializationContext ctxt) {
            JsonIntegerRange range = ctxt.readValue(p, JsonIntegerRange.class);
            return JPIntegerRange.create(range.getLower(), range.getUpper());
          }
        })
        // String to DateRange
        .addDeserializer(JPDateRange.class, new StdDeserializer<>(JPDateRange.class) {
          @Override
          public JPDateRange deserialize(JsonParser p, DeserializationContext ctxt) {
            JsonDateRange range = ctxt.readValue(p, JsonDateRange.class);
            return JPDateRange.create(range.getLower(), range.getUpper());
          }
        })
        // String to DateTimeRange
        .addDeserializer(JPDateTimeRange.class, new StdDeserializer<>(JPDateTimeRange.class) {
          @Override
          public JPDateTimeRange deserialize(JsonParser p, DeserializationContext ctxt) {
            JsonDateTimeRange range = ctxt.readValue(p, JsonDateTimeRange.class);
            return JPDateTimeRange.create(range.getLower(), range.getUpper(), range.isCloseLower(), range.isCloseUpper());
          }
        })
        // String to BigDecimal
        .addDeserializer(BigDecimal.class, new StdDeserializer<>(BigDecimal.class) {
          @Override
          public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) {
            String s = p.getValueAsString();
            return StringUtils.hasText(s) ? ValueParser.parseTo(BigDecimal.class, s) : null;
          }
        })
        // String to JPIntegerArray
        .addDeserializer(JPIntegerArray.class, new StdDeserializer<>(JPIntegerArray.class) {
          @Override
          public JPIntegerArray deserialize(JsonParser p, DeserializationContext ctxt) {
            return JPIntegerArray.of(ctxt.readValue(p, Integer[].class));
          }
        })
        // String to JPLongArray
        .addDeserializer(JPLongArray.class, new StdDeserializer<>(JPLongArray.class) {
          @Override
          public JPLongArray deserialize(JsonParser p, DeserializationContext ctxt) {
            return JPLongArray.of(ctxt.readValue(p, Long[].class));
          }
        })
        // String to JPStringArray
        .addDeserializer(JPStringArray.class, new StdDeserializer<>(JPStringArray.class) {
          @Override
          public JPStringArray deserialize(JsonParser p, DeserializationContext ctxt) {
            return JPStringArray.of(ctxt.readValue(p, String[].class));
          }
        })
        // JsonString to String
        .addSerializer(JPJsonString.class,
            new ValueSerializer<>() {
              @Override
              public void serialize(JPJsonString v, JsonGenerator jGen, SerializationContext sProv) {
                jGen.writeString(v.toString());
              }
            }
        )
        // XmlString to String
        .addSerializer(JPXmlString.class,
            new ValueSerializer<>() {
              @Override
              public void serialize(JPXmlString v, JsonGenerator jGen, SerializationContext sProv) {
                jGen.writeString(v.toString());
              }
            })
        // JPSimpleFraction to String
        .addSerializer(JPSimpleFraction.class,
            new ValueSerializer<>() {
              @Override
              public void serialize(JPSimpleFraction v, JsonGenerator jGen, SerializationContext sProv) {
                JsonSimpleFraction json = new JsonSimpleFraction();
                json.setPositive(v.isPositive());

                int integer = v.getInteger();
                int numerator = v.getNumerator();

                if (numerator != 0) {
                  if (integer != 0) {
                    json.setInteger(integer);
                  }
                  json.setNumerator(numerator);
                  json.setDenominator(v.getDenominator());
                } else {
                  json.setInteger(integer);
                }
                jGen.writePOJO(json);
              }
            })
        // Money to BigDecimal
        .addSerializer(JPMoney.class,
            new ValueSerializer<>() {
              @Override
              public void serialize(JPMoney v, JsonGenerator jGen, SerializationContext sProv) {
                jGen.writePOJO(v.getNumberStripped());
              }
            })
        // IntegerRange to String
        .addSerializer(JPIntegerRange.class,
            new ValueSerializer<>() {
              @Override
              public void serialize(JPIntegerRange v, JsonGenerator jGen, SerializationContext sProv) {
                JsonIntegerRange json = new JsonIntegerRange();
                json.setLower(v.lower());
                json.setUpper(v.upper());
                jGen.writePOJO(json);
              }
            })
        // DateRange to String
        .addSerializer(JPDateRange.class,
            new ValueSerializer<>() {
              @Override
              public void serialize(JPDateRange v, JsonGenerator jGen, SerializationContext sProv) {
                JsonDateRange json = new JsonDateRange();
                json.setLower(v.lower());
                json.setUpper(v.upper());
                jGen.writePOJO(json);
              }
            })
        // DateTimeRange to String
        .addSerializer(JPDateTimeRange.class,
            new ValueSerializer<>() {
              @Override
              public void serialize(JPDateTimeRange v, JsonGenerator jGen, SerializationContext sProv) {
                JsonDateTimeRange json = new JsonDateTimeRange();
                json.setLower(v.lower());
                json.setUpper(v.upper());
                json.setCloseLower(v.isLowerBoundClosed());
                json.setCloseUpper(v.isUpperBoundClosed());
                jGen.writePOJO(json);
              }
            })
        // BigDecimal to String
        .addSerializer(BigDecimal.class,
            new ValueSerializer<>() {
              @Override
              public void serialize(BigDecimal v, JsonGenerator jGen, SerializationContext sProv) {
                // При длине > 16 символов браузер игнорирует другие цифры
                if (v.precision() > 16 || v.compareTo(MIN_SAFE_BIGDECIMAL) < 0 || v.compareTo(MAX_SAFE_BIGDECIMAL) > 0) {
                  jGen.writeString(v.toPlainString());
                } else {
                  jGen.writeNumber(new BigDecimal(v.toPlainString()));
                }
              }
            })
        // Long to String
        .addSerializer(Long.class,
            new ValueSerializer<>() {
              @Override
              public void serialize(Long v, JsonGenerator jGen, SerializationContext sProv) {
                if (v < MIN_SAFE_INTEGER || v > MAX_SAFE_INTEGER) {
                  jGen.writeString(v.toString());
                } else {
                  jGen.writeNumber(v);
                }
              }
            })
        // BigInteger to String
        .addSerializer(BigInteger.class,
            new ValueSerializer<>() {
              @Override
              public void serialize(BigInteger v, JsonGenerator jGen, SerializationContext sProv) {
                if (v.compareTo(MIN_SAFE_BIGINTEGER) < 0 || v.compareTo(MAX_SAFE_BIGINTEGER) > 0) {
                  jGen.writeString(v.toString());
                } else {
                  jGen.writeNumber(v);
                }
              }
            })
        // JPIntegerArray to String
        .addSerializer(JPIntegerArray.class,
            new ValueSerializer<>() {
              @Override
              public void serialize(JPIntegerArray v, JsonGenerator jGen, SerializationContext sProv) {
                jGen.writePOJO(v.toList());
              }
            })
        // JPLongArray to String
        .addSerializer(JPLongArray.class,
            new ValueSerializer<>() {
              @Override
              public void serialize(JPLongArray v, JsonGenerator jGen, SerializationContext sProv) {
                jGen.writePOJO(v.toList());
              }
            })
        // JPStringArray to String
        .addSerializer(JPStringArray.class,
            new ValueSerializer<>() {
              @Override
              public void serialize(JPStringArray v, JsonGenerator jGen, SerializationContext sProv) {
                jGen.writePOJO(v.toList());
              }
            });
    builder.addModule(module);
  }
}
