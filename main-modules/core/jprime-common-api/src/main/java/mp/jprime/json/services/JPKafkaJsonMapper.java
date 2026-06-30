package mp.jprime.json.services;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * Базовый класс JSON-обработчиков данных из Kafka
 */
@Service
public class JPKafkaJsonMapper extends JPBaseObjectMapper {
  private final JsonMapper kafkaJsonMapper;

  public JPKafkaJsonMapper(@Autowired JPJsonMapper jpJsonMapper) {
    kafkaJsonMapper = jpJsonMapper.getObjectMapper().rebuild()
        // Игнорируем переносы строк и прочие служебные символы
        .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
        .changeDefaultVisibility(v -> v.withFieldVisibility(JsonAutoDetect.Visibility.ANY))
        .build();
  }

  @Override
  public JsonMapper getObjectMapper() {
    return kafkaJsonMapper;
  }
}
