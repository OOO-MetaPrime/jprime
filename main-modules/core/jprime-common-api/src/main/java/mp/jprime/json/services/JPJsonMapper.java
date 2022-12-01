package mp.jprime.json.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.formats.DateFormat;
import mp.jprime.json.modules.JPObjectMapperExpander;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.TimeZone;

/**
 * Базовый класс JSON-обработчиков
 */
@Service
public class JPJsonMapper {
  private final ObjectMapper OBJECT_MAPPER;

  private JPJsonMapper(@Autowired Collection<JPObjectMapperExpander> expanders) {
    OBJECT_MAPPER = new ObjectMapper();
    // Добавляем модули
    expanders.forEach(x -> x.expand(OBJECT_MAPPER));
    OBJECT_MAPPER
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
        // Игнорируем пустые значения
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        // Игнорируем переносы строк и прочие служебные символы
        .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)
        .addMixIn(InputStream.class, MixInForIgnoreType.class)
        .setTimeZone(TimeZone.getDefault())
        //  ISO8601
        .setDateFormat(new SimpleDateFormat(DateFormat.ISO8601));
  }

  public ObjectMapper getObjectMapper() {
    return OBJECT_MAPPER;
  }

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

  public <T> T toObject(Class<T> to, String value) {
    if (value == null) {
      return null;
    }
    if (to == null) {
      throw new IllegalArgumentException("Unset destination type <to> on call JPJsonMapper");
    }
    try {
      return getObjectMapper().readValue(value, to);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  public <T> T toObject(TypeReference<T> to, String value) {
    if (value == null) {
      return null;
    }
    if (to == null) {
      throw new IllegalArgumentException("Unset destination type <to> on call JPJsonMapper");
    }
    try {
      return getObjectMapper().readValue(value, to);
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }
}
