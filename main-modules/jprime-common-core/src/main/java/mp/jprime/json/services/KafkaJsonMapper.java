package mp.jprime.json.services;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Базовый класс JSON-обработчиков данных из Kafka
 */
public interface KafkaJsonMapper extends JsonMapper {
  ObjectMapper KAFKA_OBJECT_MAPPER = OBJECT_MAPPER.copy()
      // Игнорируем переносы строк и прочие служебные символы
      .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)
      .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
}
