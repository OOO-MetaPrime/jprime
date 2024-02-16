package mp.jprime.json.services;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import mp.jprime.formats.DateFormat;
import mp.jprime.json.modules.JPObjectMapperExpander;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.TimeZone;

public abstract class JPObjectMapper extends JPBaseObjectMapper {

  protected void setSettings(Collection<JPObjectMapperExpander> expanders, ObjectMapper mapper) {
    // Добавляем модули
    expanders.forEach(x -> x.expand(mapper));
    mapper
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
        // Игнорируем переносы строк и прочие служебные символы
        .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)
        .addMixIn(InputStream.class, MixInForIgnoreType.class)
        .setTimeZone(TimeZone.getDefault())
        // ISO8601
        .setDateFormat(new SimpleDateFormat(DateFormat.ISO8601));
  }
}
