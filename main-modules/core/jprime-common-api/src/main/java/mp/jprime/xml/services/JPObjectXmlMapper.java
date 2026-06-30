package mp.jprime.xml.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import mp.jprime.formats.DateFormat;
import mp.jprime.json.services.JPBaseObjectMapper;
import mp.jprime.json.services.MixInForIgnoreType;
import mp.jprime.xml.modules.JPObjectMapperXmlExpander;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.cfg.MapperBuilder;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.TimeZone;

public abstract class JPObjectXmlMapper extends JPBaseObjectMapper {

  protected void setSettings(Collection<JPObjectMapperXmlExpander> expanders, MapperBuilder<?, ?> builder) {
    // Добавляем модули
    expanders.forEach(x -> x.expand(builder));
    builder
        .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
        .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
        // Игнорируем пустые значения
        .changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL))
        .addMixIn(InputStream.class, MixInForIgnoreType.class)
        .defaultTimeZone(TimeZone.getDefault())
        //  ISO8601
        .defaultDateFormat(new SimpleDateFormat(DateFormat.ISO8601));
  }
}
