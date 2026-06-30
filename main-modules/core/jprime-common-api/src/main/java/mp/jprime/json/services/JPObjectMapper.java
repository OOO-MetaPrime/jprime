package mp.jprime.json.services;

import mp.jprime.formats.DateFormat;
import mp.jprime.json.modules.JPObjectMapperExpander;
import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.TimeZone;

public abstract class JPObjectMapper extends JPBaseObjectMapper {

  protected void setSettings(Collection<JPObjectMapperExpander> expanders, JsonMapper.Builder builder) {
    // Добавляем модули
    expanders.forEach(x -> x.expand(builder));
    builder
        .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
        .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
        // Игнорируем переносы строк и прочие служебные символы
        .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
        .addMixIn(InputStream.class, MixInForIgnoreType.class)
        .defaultTimeZone(TimeZone.getDefault())
        // ISO8601
        .defaultDateFormat(new SimpleDateFormat(DateFormat.ISO8601));
  }
}
