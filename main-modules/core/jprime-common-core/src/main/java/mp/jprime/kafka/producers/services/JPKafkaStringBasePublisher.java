package mp.jprime.kafka.producers.services;

import mp.jprime.json.services.JPKafkaJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaOperations;

import static mp.jprime.kafka.producers.configs.JPBaseStringKafkaProducerConfig.JP_BASE_STRING_KAFKA_PRODUCER_CONFIG;

/**
 * Типовой сервис отправки событий в Kafka, с типом ключа и значения - {@link String}
 *
 * @param <E> тип события
 * @param <J> тип JSON-бина события
 */
public abstract class JPKafkaStringBasePublisher<E, J> extends JPKafkaBasePublisher<E, String, String> {

  private JPKafkaJsonMapper jsonMapper;

  private KafkaOperations<String, String> defaultKafkaOperations;

  @Autowired(required = false)
  @Qualifier(JP_BASE_STRING_KAFKA_PRODUCER_CONFIG)
  private void setDefaultKafkaOperations(KafkaOperations<String, String> defaultKafkaOperations) {
    this.defaultKafkaOperations = defaultKafkaOperations;
  }

  @Autowired
  private void setJsonMapper(JPKafkaJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  @Override
  protected KafkaOperations<String, String> getKafkaOperations() {
    return defaultKafkaOperations;
  }

  @Override
  protected String toValue(E event) {
    J json = toJson(event);
    return json instanceof String ? ((String) json) : jsonMapper.toString(json);
  }

  /**
   * Конвертирует событие в JSON-бин события
   *
   * @param event событие
   * @return JSON-бин события
   */
  protected abstract J toJson(E event);
}
