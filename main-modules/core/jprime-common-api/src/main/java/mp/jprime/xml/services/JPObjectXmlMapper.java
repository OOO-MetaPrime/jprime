package mp.jprime.xml.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import mp.jprime.formats.DateFormat;
import mp.jprime.json.services.JPBaseJsonMapper;
import mp.jprime.json.services.MixInForIgnoreType;
import mp.jprime.xml.modules.JPObjectMapperXmlExpander;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.TimeZone;

public abstract class JPObjectXmlMapper extends JPBaseJsonMapper {

  protected void setSettings(Collection<JPObjectMapperXmlExpander> expanders, ObjectMapper mapper) {
    // Добавляем модули
    expanders.forEach(x -> x.expand(mapper));
    mapper
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
}
