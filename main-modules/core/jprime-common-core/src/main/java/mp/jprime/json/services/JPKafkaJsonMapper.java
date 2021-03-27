package mp.jprime.json.services;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Базовый класс JSON-обработчиков данных из Kafka
 */
@Service
public class JPKafkaJsonMapper {
  private final ObjectMapper KAFKA_OBJECT_MAPPER;

  public JPKafkaJsonMapper(@Autowired JPJsonMapper jpJsonMapper) {
    KAFKA_OBJECT_MAPPER = jpJsonMapper.getObjectMapper().copy()
        // Игнорируем переносы строк и прочие служебные символы
        .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)
        .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
  }

  public ObjectMapper getObjectMapper() {
    return KAFKA_OBJECT_MAPPER;
  }
}
