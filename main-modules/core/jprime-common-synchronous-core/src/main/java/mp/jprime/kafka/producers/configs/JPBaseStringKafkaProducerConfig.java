package mp.jprime.kafka.producers.configs;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Базовая конфигурация публикации событий в Kafka
 */
@Configuration
@Lazy(value = false)
public class JPBaseStringKafkaProducerConfig {

  public static final String JP_BASE_STRING_KAFKA_PRODUCER_CONFIG = "jpBaseStringKafkaProducerConfig";

  @Value("${jprime.kafka.kafkaServers:}")
  private String bootstrapAddress;

  @Value("${jprime.kafka.producers.compressionType:none}")
  private String compressionType;

  @Bean(name = JP_BASE_STRING_KAFKA_PRODUCER_CONFIG)
  public KafkaOperations<String, String> getKafkaTemplate() {
    ProducerFactory<String, String> kafkaProducerFactory = getKafkaProducerFactory();
    return kafkaProducerFactory == null ? null : new KafkaTemplate<>(kafkaProducerFactory);
  }

  private ProducerFactory<String, String> getKafkaProducerFactory() {
    if (StringUtils.isBlank(bootstrapAddress)) {
      return null;
    }
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, compressionType);
    return new DefaultKafkaProducerFactory<>(props);
  }
}
